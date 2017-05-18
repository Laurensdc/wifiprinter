<?php
require("../config.php");
require("../functions.php");

header('Content-Type: application/json');

$method = $_SERVER['REQUEST_METHOD']; // HTTP Request method
$input = file_get_contents("php://input"); // HTTP Request body (raw)

if($method != 'POST') {
    error('This route accepts POST requests only');
}

if(!$input) {
    error('No data provided');
}

$json = json_decode($input, true);

// Object checks
if(!isset($json['bill_id'])) {
    error('Missing bill_id');
}

if(!isset($json['table_nr'])) {
    error('Missing table_nr');
}

$bill_id = $json['bill_id'];
$table_nr = $json['table_nr'];

try {
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

    $stmt = $conn->prepare("UPDATE app_bills SET table_nr=(:table_nr) WHERE id=(:bill_id)");
    $stmt->bindParam(':bill_id', $bill_id);
    $stmt->bindParam(':table_nr', $table_nr);
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
