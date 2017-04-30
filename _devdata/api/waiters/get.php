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

    $stmt = $conn->prepare("SELECT * FROM app_waiters");
    $stmt->execute();
    $result = $stmt->fetchAll();
    $returnObj = array('success' => 'true', 'waiters' => utf8ize($result));

    header('Content-Type: application/json');
    echo json_encode($returnObj);
    $conn = null;
    die();
}
catch(PDOException $e) {
    error($e->getMessage());
}
