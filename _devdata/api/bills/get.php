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

    $stmt = $conn->prepare("SELECT * FROM app_bills");

    $stmt->execute();
    $result = $stmt->fetchAll();

    header('Content-Type: application/json');
    echo json_encode(utf8ize($result));

    die();
}
catch(PDOException $e) {
    echo "Error api: " . $e->getMessage();
}
$conn = null;


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
