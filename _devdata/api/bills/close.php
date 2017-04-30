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

if(!isset($json['id'])) {
    error('No id passed');
}

$id = $json['id'];

try {
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

    $stmt = $conn->prepare("UPDATE app_bills SET is_open = 0 WHERE id = :id");
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
    error($e->getMessage());
}
