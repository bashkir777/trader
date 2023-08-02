import socket
import subprocess
import time
from os import system

from time import sleep
import json

import pyuac
from binance.client import Client


def make_json(answer):
    data_for_json = {"answer": answer}
    json_obj = json.dumps(data_for_json)
    return json_obj


def vpn_off():
    system("taskkill /f /IM PlanetVPN.exe")
    sleep(0.1)
    system("taskkill /IM openvpn.exe /F")


def vpn_on():
    subprocess.Popen("D:\\PlanetVPN\\PlanetVPN", shell=True)


def send_answer_to_bot(sock, target_address, answer):
    try:
        sock.sendto(str(make_json(answer)).encode(), target_address)
    except Exception as e:
        print(e)


def get_quantity_of_all_active_orders(client, token, id_arr):
    summ = 0
    for i in id_arr:
        answer = client.futures_get_all_orders(symbol=token, orderId=i)[0]
        if answer["status"] == "FILLED":
            summ += float(answer['origQty'])
    return summ


def get_balance_in_usdt(client):
    info = client.futures_account_balance()
    for d in info:
        if d["asset"] == "USDT":
            return float(d["balance"])


def clean(client, token_name, id_arr):
    for i in id_arr:
        client.futures_cancel_order(symbol=token_name, orderId=i)


def cancel_order_by_id(client, order_id):
    client.futures_cancel_order(orderId=order_id)


def get_order_info(client, order_id):
    answer = client.futures_get_all_orders(orderId=order_id)[0]
    status = answer["status"]
    quantity = answer["origQty"]
    token_name = answer["symbol"]
    side = answer["side"]
    return status, float(quantity), token_name, side


def create_long_order(client, token_name, price, quantity):
    try:
        long_order_id = client.futures_create_order(
            symbol=token_name,
            timeInForce='GTC',
            type='LIMIT',
            price=float(round(price, float_point[token_name]['price'])),
            side='BUY',
            quantity=float(round(quantity, float_point[token_name]['quantity'])),
            reduceOnly='false'
        )
        return long_order_id
    except Exception as exception:
        print(exception)
        return 0


def create_short_order(client, token_name, price, quantity):
    try:
        short_order_id = client.futures_create_order(
            symbol=token_name,
            timeInForce='GTC',
            type='LIMIT',
            price=float(round(price, float_point[token_name]['price'])),
            side='SELL',
            quantity=float(round(quantity, float_point[token_name]['quantity'])),
            reduceOnly='false'
        )
        return short_order_id
    except Exception as exception:
        print(exception)
        return 0


def is_position_closed(client, token):
    answer = client.futures_get_all_orders(symbol=token)
    for _ in answer:
        if _["status"] == 'NEW' and _['side'] == "BUY":
            return False
    return True


def close_position(client, order_id, price):
    tup = get_order_info(client, order_id)
    side = "SELL" if tup[3] == "BUY" else "BUY"
    if tup[0] != 'FILLED':
        return 0
    else:

        close_order = client.futures_create_order(
            symbol=tup[2],
            timeInForce='GTC',
            type='LIMIT',
            price=float(round(price, float_point[tup[2]]['price'])),
            side=side,
            quantity=float(round(tup[1], float_point[tup[1]]['quantity'])),
            reduceOnly='true'
        )
        return close_order


def is_position_closed_long(client, token):
    answer = client.futures_get_all_orders(symbol=token)
    for _ in answer:
        if _["status"] == 'NEW' and _['side'] == "SELL":
            return False
    return True


def count_all_filled_orders(client, token, id_arr):
    counter = 0
    for i in id_arr:
        answer = client.futures_get_all_orders(symbol=token, orderId=i)[0]
        if answer["status"] == "FILLED":
            counter += 1
    return counter


def count_all_active_orders(client, token, id_arr):
    counter = 0
    for i in id_arr:
        answer = client.futures_get_all_orders(symbol=token, orderId=i)[0]
        if answer["status"] == "NEW":
            counter += 1
    return counter


float_point = {
    'ONTUSDT': {'price': 4, "quantity": 1},
    'ADAUSDT': {'price': 4, "quantity": 0},
    'BATUSDT': {'price': 4, "quantity": 1},
    'XRPUSDT': {'price': 4, 'quantity': 1},
    'DASHUSDT': {'price': 2, 'quantity': 3},
    'LPTUSDT': {'price': 3, 'quantity': 1},
    'FXSUSDT': {'price': 3, 'quantity': 1},
    'CHRUSDT': {'price': 4, 'quantity': 0},
    'DUSKUSDT': {'price': 5, 'quantity': 0},
    'CELOUSDT': {'price': 3, 'quantity': 1},
    'PHBUSDT': {'price': 4, 'quantity': 0},
    'ZECUSDT': {'price': 2, 'quantity': 3},
    'IMXUSDT': {'price': 4, 'quantity': 0},
    'TRXUSDT': {'price': 5, 'quantity': 0},
    'HIGHUSDT': {'price': 3, 'quantity': 1},
    'BTCUSDT': {'price': 1, 'quantity': 3},
    'LTCUSDT': {'price': 2, 'quantity': 3},
    'ARBUSDT': {'price': 4, 'quantity': 1},
    'SOLUSDT': {'price': 3, 'quantity': 0},
    'NEARUSDT': {'price': 3, 'quantity': 0},
    'BCHUSDT': {'price': 2, 'quantity': 3},
    'EOSUSDT': {'price': 3, 'quantity': 1},
    'DOTUSDT': {'price': 3, 'quantity': 1},
    'MKRUSDT': {'price': 1, 'quantity': 3},
    'LINAUSDT': {'price': 5, 'quantity': 3},
    'MTLUSDT': {'price': 4, 'quantity': 3},
    'WAVESUSDT': {'price': 4, 'quantity': 3},
    'FTMUSDT': {'price': 4, 'quantity': 3},
    'IOSTUSDT': {'price': 6, 'quantity': 0},
}

SECRET_KEY = 'SxPnXc40hnq9rNlHLVRpN4MFFdPP1dJr4gRsoXPCFjZw1pjfKy4y5KGzxAs0CEHi'
API_KEY = 'xmuUEQYBfJmggDlhLXcVsYL1ZK6bU5XDNHmVpBFcdwl7Zi8855pW5VfuBF4vTdyz'


def main():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    print("Введите порт, на котором будет работать сервер: ", end="")
    port = int(input())
    server_address = ('localhost', port)
    server_socket.bind(server_address)
    client = Client(API_KEY, SECRET_KEY)

    while True:
        data, address = server_socket.recvfrom(1024)
        json_obj = json.loads(data.decode())
        try:
            if json_obj["commandType"] == "no_args":
                command = str(json_obj['command'])
                if command == "balance":
                    send_answer_to_bot(server_socket, address, str(get_balance_in_usdt(client)))
                elif command == "vpn_on":
                    try:
                        vpn_on()
                        send_answer_to_bot(server_socket, address, "успешно")
                    except Exception as e2:
                        send_answer_to_bot(server_socket, address, "не успешно")
                        print(e2)
                elif command == "vpn_off":
                    try:
                        vpn_off()
                        send_answer_to_bot(server_socket, address, "успешно")
                    except Exception as e2:
                        send_answer_to_bot(server_socket, address, "не успешно")
                        print(e2)
                else:
                    print("Была получена не существующая команда " + command)

            elif json_obj["commandType"] == "application":
                order_type = str(json_obj["typeOperation"])
                price = float(json_obj["price"])
                quantity = float(json_obj["quantity"])
                ticker = json_obj["ticker"]
                if order_type == "long":
                    order_id = create_long_order(client, ticker, price, quantity)
                elif order_type == "short":
                    order_id = create_short_order(client, ticker, price, quantity)
                else:
                    raise Exception("Не существующий тип заявки")
                if order_id != 0:
                    send_answer_to_bot(server_socket, address, str(order_id))
            elif json_obj["commandType"] == "one_arg":
                arg = str(json_obj["argument"])
                command = str(json_obj["command"])
                if command == "is_filled":
                    if str(get_order_info(client, int(arg))[0]) == "FILLED":
                        send_answer_to_bot(server_socket, address, "true")
                    else:
                        send_answer_to_bot(server_socket, address, "false")
                elif command == "cancel":
                    try:
                        cancel_order_by_id(int(arg))
                        send_answer_to_bot(server_socket, address, "true")
                    except Exception as e:
                        print(e)
                        send_answer_to_bot(server_socket, address, "false")
                else:
                    raise Exception("Не существующий тип заявки")
            elif json_obj["commandType"] == "two_arg":
                command = str(json_obj["command"])
                arg_1 = str(json_obj["argument1"])
                arg_2 = str(json_obj["argument2"])
                if command == "close":
                    order_id = close_position(client, arg_1, arg_2)
                    send_answer_to_bot(server_socket, address, str(order_id))

            else:
                raise Exception("Не существующая команда")

        except Exception as e:
            print(e)
            try:
                send_answer_to_bot(server_socket, address, "exception")
            except Exception as e2:
                print(e2)
                print("Не удалось отправить ответ боту")


if not pyuac.isUserAdmin():
    pyuac.runAsAdmin()
else:
    main()
