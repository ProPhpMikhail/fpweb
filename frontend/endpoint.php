<?php

error_reporting(E_ERROR | E_PARSE);

header("Access-Control-Allow-Origin: http://localhost");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Credentials: true");
header("Access-Control-Allow-Headers: Content-Type");
header('Content-Type: application/json');

session_start();

if (!$_SESSION['transactions']) {
    $_SESSION['transactions'] = [];
}
$result = ['success' => false, 'message' => 'Wrong request'];
try {
    $request = json_decode(file_get_contents('php://input'), true);
    switch ($request['type']) {
        case 'getTransactions':
            $result = ['message' => $_SESSION['transactions']];
            break;
        case 'addTransaction':
            $request['name'] = trim($request['name']);
            $request['amount'] = abs(floor(trim($request['amount']) * 100) / 100);
            $id = time();
            if (!$request['name']) {
                throw new \Exception('Empty name');
            }
            if (!$request['amount']) {
                throw new \Exception('Empty amount');
            }
            $_SESSION['transactions'][] = [
                'id' => $id,
                'name' => $request['name'],
                'amount' => $request['amount'],
            ];
            $result = ['message' => 'Transaction was added', 'success' => true];
            break;
        case 'updateTransaction':
            foreach (['id', 'name', 'amount'] as $field) {
                if (!$request[$field]) {
                    throw new \Exception('No transaction ' . $field);
                }
            }
            foreach ($_SESSION['transactions'] as $index => $transaction) {
                if ($transaction['id'] == intval($request['id'])) {
                    $_SESSION['transactions'][$index]['name'] = $request['name'] ?: $_SESSION['transactions'][$index]['name'];
                    $_SESSION['transactions'][$index]['amount'] = $request['amount'] ?: $_SESSION['transactions'][$index]['amount'];
                }
                $result = ['message' => 'Transaction was updated', 'success' => true];
            }
            break;
        case 'deleteTransaction':
            foreach (['id'] as $field) {
                if (!$request[$field]) {
                    throw new \Exception('No transaction ' . $field);
                }
            }
            foreach ($_SESSION['transactions'] as $index => $transaction) {
                if ($transaction['id'] == intval($request['id'])) {
                    unset($_SESSION['transactions'][$index]);
                }
                $result = ['message' => 'Transaction was deleted', 'success' => true];
            }
            break;
        case 'deleteTransactions':
            $_SESSION['transactions'] = [];
            $result = ['message' => 'Transactions were deleted', 'success' => true];
            break;
    }
} catch (\Exception $exception) {
    $result = ['success' => false, 'message' => $exception->getMessage()];
}
die(json_encode($result));
