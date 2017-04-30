<?php
require("../config.php");

$servername = DB_HOST;
$username = DB_USER;
$password = DB_PASSWORD;
$dbname = DB_NAME;

$method = $_SERVER['REQUEST_METHOD'];

if($method != 'GET') {
    echo 'This route accepts GET requests only';
    die();
}

try {
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password, $opt);

    $stmt = $conn->prepare("SELECT * FROM app_bills WHERE is_open = 1");

    $stmt->execute();
    $result = $stmt->fetchAll();
    $returnObj = array('success' => 'true', 'bills' => utf8ize($result));

    header('Content-Type: application/json');
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

function utf8ize($d) {
    if (is_array($d)) {
        foreach ($d as $k => $v) {
            $d[$k] = utf8ize($v);
        }
    } else if (is_string ($d)) {
        return utf8_encode($d);
    }
    return $d;
}
