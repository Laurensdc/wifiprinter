<?php
require("../config.php");

$servername = DB_HOST;
$username = DB_USER;
$password = DB_PASSWORD;
$dbname = DB_NAME;

$method = $_SERVER['REQUEST_METHOD']; // HTTP Request method
$input = file_get_contents("php://input"); // HTTP Request body (raw)

if($method != 'POST') {
    echo 'This route accepts POST requests only';
    die();
}

if(!$input) {
    echo 'No data provided';
    die();
}

$json = json_decode($input, true);

// Object checks
if(!isset($json['waiter'])) {
    echo 'No waiter object provided';
    die();
}

$waiter = $json['waiter'];

if(!isset($waiter['name'])) {
    echo 'No waiter name passed';
    die();
}

try {
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password, $opt);

    $stmt = $conn->prepare("INSERT INTO app_waiters (name) VALUES (:name)");
    $stmt->bindParam(':name', $waiter['name']);
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
    $returnObj = array('success' => false, 'message' => $e->getMessage());
    echo json_encode($returnObj);
    $conn = null;
    die();
}
