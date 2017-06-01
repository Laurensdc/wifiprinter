<?php
require("../config.php");
require("../functions.php");

header('Content-Type: application/json');

$method = $_SERVER['REQUEST_METHOD']; // HTTP Request method
$input = file_get_contents("php://input"); // HTTP Request body (raw)

if(!$input) {
    error('No data provided');
}

if($method == 'PUT') {
    $json = json_decode($input, true);

    // Object checks
    if(!isset($json['bill_id'])) {
        error('Missing bill_id');
    }

    if(!isset($json['total_price_excl'])) {
        error('Missing total_price_excl');
    }

    $bill_id = $json['bill_id'];
    $total_price_excl = $json['total_price_excl'];

    /*** Add a product on a bill***

Request body format:

{
    
        "bill_id":int,
        "total_price_excl": decimal(4,2)
    
}
*/
 try {
        $opt = [
            PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES   => false,
        ];

        $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

        $stmt2 = $conn->prepare("UPDATE app_bills SET total_price_excl = :total_price_excl WHERE id = :bill_id");
        $stmt2->bindParam(':total_price_excl', $total_price_excl);
        $stmt2->bindParam(':bill_id', $bill_id);
        $stmt2->execute();

        $rowcount2 = $stmt2->rowCount();
        $success2 = ($rowcount2 > 0) ? true : false;
        $returnObj2 = array('success' => $success2);

        echo json_encode($returnObj2);
        
       
        

        $conn = null;
        die();
    }
    catch(PDOException $e) {
        error($e->getMessage());
    }

}
else if($method == 'DELETE') {

    $json = json_decode($input, true);

    // Object checks
    if(!isset($json['bill_id'])) {
        error('Missing bill_id');
    }

    if(!isset($json['product_id'])) {
        error('Missing product_id');
    }

    $bill_id = $json['bill_id'];
    $product_id = $json['product_id'];

        /*** delete a product from a bill***

Request body format:

{
    {
        "bill_id":int,
        "product_id":int
    }
}

*/

    try {
        $opt = [
            PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES   => false,
        ];

        $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

        $stmt = $conn->prepare("DELETE FROM app_bill_has_products WHERE bill_id = :bill_id AND product_id = :product_id");
        $stmt->bindParam(':bill_id', $bill_id);
        $stmt->bindParam(':product_id', $product_id);
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
    error('This route accepts PUT and DELETE requests only');
}
