<?php
require("../config.php");
require("../functions.php");

$method = $_SERVER['REQUEST_METHOD']; // HTTP Request method
$input = file_get_contents("php://input"); // HTTP Request body (raw)

if($method != 'POST') {
    error('This route accepts POST requests only');
}

if(!$input) {
    error('No data provided');
}

$json = json_decode($input, true);


if(!isset($json['name'])) {
    error('No waiter name passed');
}

/*** Add a waiter ***

Request body format:

{
        "name":string
    
}

*/

try {
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

    $stmt = $conn->prepare("INSERT INTO app_waiters (name) VALUES (:name)");
    $stmt->bindParam(':name', $json['name']);
    $success = $stmt->execute();

    $stmt2 = $conn->prepare("SELECT LAST_INSERT_ID() AS id");
    $stmt2->execute();
    $id = $stmt2->fetchAll();

    $returnObj = array('id' => $id[0]["id"], 'success' => $success);

    echo json_encode($returnObj);
    $conn = null;
    die();
}
catch(PDOException $e) {
    error($e->getMessage());
}
