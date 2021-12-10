def get_data(input_body):
    order_id_dict = {}
    graph_dict = {}
    demand_dict = {}

    for i, v in enumerate(input_body['points']):
        order_id_dict[i + 1] = v['orderId']
        graph_dict[i + 1] = (v['latitude'], v['longitude'])
        demand_dict[i + 1] = v['quantity']

    print("Received points:")
    for i in input_body['points']:
        print(i)

    return int(input_body['vehicleCapacity']), graph_dict, demand_dict, order_id_dict
