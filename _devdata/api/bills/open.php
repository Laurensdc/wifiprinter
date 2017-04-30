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
if(!isset($json['id'])) {
    echo 'No id object provided';
    die();
}

$id = $json['id'];

try {
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password, $opt);

    $stmt = $conn->prepare("UPDATE app_bills SET is_open = 1 WHERE id = :id");
    $stmt->bindParam(':id', $id);
    $stmt->execute();
    $rowcount = $stmt->rowCount();

    $success = ($rowcount > 0) ? true : false;
    $returnObj = array('success' => $success);

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
