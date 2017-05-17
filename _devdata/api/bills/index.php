<?php
require("../config.php");
require("../functions.php");

$method = $_SERVER['REQUEST_METHOD']; // HTTP Request method
$input = file_get_contents("php://input"); // HTTP Request body (raw)

/*** Get all bills ***/
if($method == 'GET') {
    try {
        $opt = [
            PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES   => false,
        ];

        $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

        $stmt = $conn->prepare("SELECT * FROM app_bills");
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

}
/*** Add a bill ***

Request body format:

{
    bill: {
        table_nr: int
    }
}

*/
else if($method == 'PUT') {

    if(!$input) {
        error('No data provided');
    }

    $json = json_decode($input, true);

    Object checks
    if(!isset($json['bill'])) {
       error('No bill object provided CHANGE');
    }

    $bill = $json['bill'];

    if(!isset($bill['table_nr'])) {
       error('No table_nr provided CHANGE');
    }

    // if(!isset($json['table_nr'])) {
    //     error('No bill object provided CHANGE');
    // }

    try {
        $opt = [
            PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES   => false,
        ];

        $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

        $stmt = $conn->prepare("INSERT INTO app_bills (table_nr) VALUES (:table_nr)");
        $stmt->bindParam(':table_nr', $bill['table_nr']);
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

}
/*** Open or close a bill ***

Request body format:

{
    bill_id: int,
    open: bool
}

*/
else if($method == 'POST') {
    if(!$input) {
        error('No data provided');
    }

    $json = json_decode($input, true);

    if(!isset($json['bill_id'])) {
        error('No bill_id passed');
    }

    $id = $json['bill_id'];

    if(!isset($json['open'])) {
        error('Bool open not passed');
    }

    $open = $json['open'];

    try {
        $opt = [
            PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES   => false,
        ];

        $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

        $stmt = $conn->prepare("UPDATE app_bills SET is_open = :open WHERE id = :id");
        $stmt->bindParam(':open', $open);
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
}
else {
    error('This route accepts GET, PUT and POST requests only');
}
