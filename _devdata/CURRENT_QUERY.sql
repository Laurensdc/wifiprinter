SELECT
        cat.id_category AS 'id_cat',
        catl.name AS 'name_cat',
        p.id_product AS 'id_prod',
        pl.name AS 'name_prod',
        ROUND(p.price, 2) AS 'price_prod_excl',
        ROUND(p.price * (COALESCE(ptx.rate, 0) / 100 + 1), 2) AS 'price_prod_incl',
        pl.description_short AS 'description_prod'

FROM        ps_product p
INNER JOIN  ps_product_lang pl ON p.id_product = pl.id_product
INNER JOIN  ps_category_product cat ON p.id_product = cat.id_product
INNER JOIN  ps_category_lang catl ON cat.id_category = catl.id_category
INNER JOIN  ps_tax_rule ptxgrp ON ptxgrp.id_tax_rules_group = p.id_tax_rules_group
INNER JOIN  ps_tax ptx ON ptx.id_tax = ptxgrp.id_tax
ORDER BY cat.id_category

;
