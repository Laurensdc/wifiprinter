SELECT CONCAT('PS', p.id_product) AS 'sku',
    p.active AS 'Active (0/1)',
    pl.name AS 'post_title',
    GROUP_CONCAT(DISTINCT REPLACE(pc.name, ' / ', '>')  SEPARATOR '|') AS 'tax:product_cat',
    p.price AS 'Price tax excl.',
    ROUND(p.price * (COALESCE(ptx.rate, 0) / 100 + 1), 2) AS 'regular_price',
    COALESCE(ptx.rate, 0) AS 'Tax',
    p.wholesale_price AS 'Wholesale price',
    p.on_sale AS 'On sale (0/1)',
    p.reference AS 'Reference #',
    p.supplier_reference AS 'Supplier reference #',
    p.ean13 AS 'EAN13',
    p.upc AS 'UPC',
    p.ecotax AS 'Ecotax',
    p.weight AS 'weight',
    p.quantity AS 'stock',
    "yes" AS 'manage_stock',
    pl.description_short AS 'post_excerpt',
    pl.description AS 'post_content',
    pl.meta_title AS 'Meta-title',
    pl.meta_keywords AS 'Meta-keywords',
    pl.meta_description AS 'Meta-description',
    pl.link_rewrite AS 'URL rewritten',
    pl.available_now AS 'Text when in stock',
    pl.available_later AS 'Text when backorder allowed',
    p.available_for_order AS 'Available for order',
    p.date_add AS 'post_date',
    p.show_price AS 'Show price',
    p.online_only AS 'Available online only',
    p.`condition` AS 'Condition',
    concat( 'http://svartrecords.com/shoppe/img/p/',mid(im.id_image,1,1),'/', if (length(im.id_image)>1,concat(mid(im.id_image,2,1),'/'),''),if (length(im.id_image)>2,concat(mid(im.id_image,3,1),'/'),''),if (length(im.id_image)>3,concat(mid(im.id_image,4,1),'/'),''),if (length(im.id_image)>4,concat(mid(im.id_image,5,1),'/'),''), im.id_image, '.jpg' ) AS 'images'
FROM ps_product p LEFT JOIN
ps_product_lang pl ON p.id_product = pl.id_product AND pl.id_lang = 1 JOIN
ps_tax_rule ptxgrp ON ptxgrp.id_tax_rules_group = p.id_tax_rules_group JOIN
ps_tax ptx ON ptx.id_tax = ptxgrp.id_tax  JOIN
ps_image im ON p.id_product = im.id_product JOIN
ps_category_product pcp ON pcp.id_product = p.id_product JOIN
ps_category_lang pc ON pcp.id_category = pc.id_category AND pc.id_lang = 1
WHERE p.active = 1
GROUP BY p.id_product

;
