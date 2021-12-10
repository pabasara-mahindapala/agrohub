import getopt
import sys
import time
from functools import reduce
from math import sin, cos, sqrt, atan2, radians
import googlemaps
import numpy

from aco_code import data_formatter

# Requires API key
gmaps = googlemaps.Client(key='<GOOGLE_API_KEY>')

alfa = 4
beta = 8
sigma = 6
ro = 0.9
th = 100
iterations = 1000
ants = 22

i = 0


def getDistLongLat(a_x, a_y, b_x, b_y):
    R = 6373.0

    lat1 = radians(a_x)
    lon1 = radians(a_y)
    lat2 = radians(b_x)
    lon2 = radians(b_y)

    dlon = lon2 - lon1
    dlat = lat2 - lat1

    a = sin(dlat / 2) ** 2 + cos(lat1) * cos(lat2) * sin(dlon / 2) ** 2
    c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c


def getDist(a_x, a_y, b_x, b_y):
    origin_latitude = a_x
    origin_longitude = a_y
    destination_latitude = b_x
    destination_longitude = b_y
    result = gmaps.distance_matrix([str(origin_latitude) + " " + str(origin_longitude)],
                                   [str(destination_latitude) + " " + str(destination_longitude)], mode='driving')[
        'rows'][0]['elements'][0]

    if 'distance' in result.keys():
        dist = result['distance']
        # print(dist['value'])
        return dist['value'] / 1000
    else:
        print(getDistLongLat(a_x, a_y, b_x, b_y))
        return getDistLongLat(a_x, a_y, b_x, b_y)

    # return getDistLongLat(a_x, a_y, b_x, b_y)


def generateGraph(input_body):
    # Input the values here
    capacityLimit, graph, demand, order_id_dict = data_formatter.get_data(input_body)
    ants = len(order_id_dict) * 50
    vertices = list(graph.keys())
    vertices.remove(1)

    edges = {
        (min(a, b), max(a, b)): getDist(graph[a][0], graph[a][1], graph[b][0], graph[b][1]) for a in
        graph.keys() for b in graph.keys()
    }
    pheromones = {(min(a, b), max(a, b)): 1 for a in graph.keys() for b in graph.keys() if a != b}

    return vertices, edges, capacityLimit, demand, pheromones, order_id_dict


def solutionOfOneAnt(vertices, edges, capacityLimit, demand, feromones):
    solution = list()

    while len(vertices) != 0:
        path = list()
        city = numpy.random.choice(vertices)
        capacity = capacityLimit - demand[city]
        path.append(city)
        vertices.remove(city)
        while len(vertices) != 0:
            probabilities = list(map(lambda x: ((feromones[(min(x, city), max(x, city))]) ** alfa) * (
                    (1 / edges[(min(x, city), max(x, city))]) ** beta), vertices))
            probabilities = probabilities / numpy.sum(probabilities)

            city = numpy.random.choice(vertices, p=probabilities)
            capacity = capacity - demand[city]

            if capacity > 0:
                path.append(city)
                vertices.remove(city)
            else:
                break
        solution.append(path)
    return solution


def rateSolution(solution, edges):
    s = 0
    for i in solution:
        a = 1
        for j in i:
            b = j
            s = s + edges[(min(a, b), max(a, b))]
            a = b
        b = 1
        s = s + edges[(min(a, b), max(a, b))]
    return s


def updateFeromone(feromones, solutions, bestSolution):
    Lavg = reduce(lambda x, y: x + y, (i[1] for i in solutions)) / len(solutions)
    feromones = {k: (ro + th / Lavg) * v for (k, v) in feromones.items()}
    solutions.sort(key=lambda x: x[1])
    if bestSolution != None:
        if solutions[0][1] < bestSolution[1]:
            bestSolution = solutions[0]
        for path in bestSolution[0]:
            for i in range(len(path) - 1):
                feromones[(min(path[i], path[i + 1]), max(path[i], path[i + 1]))] = sigma / bestSolution[1] + feromones[
                    (min(path[i], path[i + 1]), max(path[i], path[i + 1]))]
    else:
        bestSolution = solutions[0]
    for l in range(sigma):
        paths = solutions[l][0]
        L = solutions[l][1]
        for path in paths:
            for i in range(len(path) - 1):
                feromones[(min(path[i], path[i + 1]), max(path[i], path[i + 1]))] = (sigma - (l + 1) / L ** (l + 1)) + \
                                                                                    feromones[(
                                                                                        min(path[i], path[i + 1]),
                                                                                        max(path[i], path[i + 1]))]
    return bestSolution


def get_routes(solution, order_id_dict):
    routes = []

    for a, route in enumerate(solution):
        points = []
        for b, point in enumerate(route):
            points.append(order_id_dict[point])
        routes.append({
            'orderIdsRouted': points
        })

    print("Generated routes:")
    print(routes)

    return routes


def aco(input_body):
    bestSolution = None
    vertices, edges, capacityLimit, demand, feromones, order_id_dict = generateGraph(input_body)

    for i in range(iterations):
        solutions = list()
        for _ in range(ants):
            solution = solutionOfOneAnt(vertices.copy(), edges, capacityLimit, demand, feromones)
            solutions.append((solution, rateSolution(solution, edges)))
        bestSolution = updateFeromone(feromones, solutions, bestSolution)
        print(str(i) + ":\t" + str(int(bestSolution[1])))

    print("Best solution:")
    print(bestSolution[0])
    print("\n")

    return get_routes(bestSolution[0], order_id_dict)
