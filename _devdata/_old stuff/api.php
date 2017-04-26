<?php

require("config.php");


// get the HTTP method, path and body of the request
$method = $_SERVER['REQUEST_METHOD'];
//$request = explode('/', trim($_SERVER['PATH_INFO'],'/'));
$input = json_decode(file_get_contents('php://input'),true);

$function = $_GET['function'];



$servername = DB_HOST;
$username = DB_USER;
$password = DB_PASSWORD;
$dbname = DB_NAME;

if($method == 'GET' && $function == 'getproducts') {
    try {
        $opt = [
          PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
          PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
          PDO::ATTR_EMULATE_PREPARES   => false,
        ];

        $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password, $opt);
        // set the PDO error mode to exception
        $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

        $stmt = $conn->prepare("SELECT DISTINCT

                            cat.id_category AS 'id_cat',
                            catl.name AS 'name_cat',
                            p.id_product AS 'id_prod',
                            pl.name AS 'name_prod',
                            p.reference AS 'reference_prod',
                            ROUND(p.price, 2) AS 'price_prod_excl',
                            ROUND(p.price * (COALESCE(ptx.rate, 0) / 100 + 1), 2) AS 'price_prod_incl',
                            pl.description_short AS 'description_prod'

                    FROM        ps_product p
                    INNER JOIN  ps_product_lang pl ON p.id_product = pl.id_product
                    INNER JOIN  ps_category_product cat ON p.id_product = cat.id_product
                    INNER JOIN  ps_category_lang catl ON cat.id_category = catl.id_category
                    INNER JOIN  ps_tax_rule ptxgrp ON ptxgrp.id_tax_rules_group = p.id_tax_rules_group
                    INNER JOIN  ps_tax ptx ON ptx.id_tax = ptxgrp.id_tax

                    ORDER BY    name_cat ASC,
                                name_prod ASC

                    ;


                                ");

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
