<?php
require("../config.php");
require("../functions.php");

$method = $_SERVER['REQUEST_METHOD'];
$input = file_get_contents("php://input"); // HTTP Request body (raw)


if($method != 'POST') {
    error('This route accepts POST requests only');
}

if(!$input) {
    error('No data provided');
}

$json = json_decode($input, true);

if(!isset($json['bill_id'])) {
    error('bill_id not passed');
}

$bill_id = $json['bill_id'];

try {
    $opt = [
        PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
        PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
        PDO::ATTR_EMULATE_PREPARES   => false,
    ];

    $conn = new PDO("mysql:host=" . DB_HOST . ";dbname=" . DB_NAME, DB_USER, DB_PASSWORD, $opt);

    $stmt = $conn->prepare("SELECT DISTINCT
        cat.id_category AS 'id_cat',
        catl.name AS 'name_cat',
        p.id_product AS 'id_prod',
        pl.name AS 'name_prod',
        p.reference AS 'reference_prod',
        #12.06 added 1 lines >
        catl.id_group AS 'id_group',
        ROUND(p.price, 2) AS 'price_prod_excl',
        ROUND( p.price * (COALESCE(ptx.rate, 0) / 100 + 1), 2) AS 'price_prod_incl',
        pl.description_short AS 'description_prod',
        app_bhs.bill_id AS 'bill_id',
        app_bhs.count AS 'count'

        FROM        ps_product p
        INNER JOIN  ps_product_lang pl ON p.id_product = pl.id_product
        INNER JOIN  ps_category_product cat ON p.id_product = cat.id_product
        INNER JOIN  ps_category_lang catl ON cat.id_category = catl.id_category
        INNER JOIN  ps_tax_rule ptxgrp ON ptxgrp.id_tax_rules_group = p.id_tax_rules_group
        INNER JOIN  ps_tax ptx ON ptx.id_tax = ptxgrp.id_tax
        INNER JOIN  app_bill_has_products app_bhs ON app_bhs.product_id = p.id_product

        WHERE bill_id = :bill_id

        ORDER BY    name_cat ASC,
                    name_prod ASC

        ;

    ");

    $stmt->bindParam(':bill_id', $bill_id);

    $stmt->execute();
    $result = $stmt->fetchAll();
    $returnObj = array('success' => 'true', 'products' => utf8ize($result));

    header('Content-Type: application/json');
    echo json_encode($returnObj);
    $conn = null;
    die();
}
catch(PDOException $e) {
    error($e->getMessage());
}
