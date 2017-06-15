<?php
require("../config.php");
require("../functions.php");

$method = $_SERVER['REQUEST_METHOD'];

if($method != 'GET') {
    error('This route accepts GET requests only');
}

try {
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

     $stmt = $conn->prepare("SELECT

        b.id AS 'id_bill',
        SUBDATE(b.date, INTERVAL 15 HOUR) AS 'date_bill',
        b.table_nr AS 'table_nr',
    b.total_price_excl AS 'total_price_excl',
        w.name AS 'waiter_name'

        FROM app_bills b 
        INNER JOIN app_bill_has_waiters bhw ON b.id = bhw.bill_id
        INNER JOIN app_waiters w ON bhw.waiter_id = w.id 

        WHERE b.is_open = 1

        /*ORDER BY date_bill ASC,
                 id_bill ASC*/

        ;

        ");

    $stmt->execute();
    $result = $stmt->fetchAll();
    $returnObj = array('success' => 'true', 'bills' => utf8ize($result));

    header('Content-Type: application/json');
    echo json_encode($returnObj);
    $conn = null;
    die();
}
catch(PDOException $e) {
    error($e->getMessage());
}
