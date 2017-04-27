<?php
require("config.php");

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
if(!isset($json['bill'])) {
    echo 'No bill object provided';
    die();
}

$bill = $json['bill'];

if(!isset($bill['table_nr'])) {
    echo 'No table nr';
    die();
}

try {
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password, $opt);

    $stmt = $conn->prepare("INSERT INTO app_bills (table_nr) VALUES (:table_nr)");
    $stmt->bindParam(':table_nr', $bill['table_nr']);

    $stmt->execute();

    $stmt2 = $conn->prepare("SELECT LAST_INSERT_ID() AS id");
    $stmt2->execute();
    $result = $stmt2->fetchAll();

    echo json_encode($result);

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
