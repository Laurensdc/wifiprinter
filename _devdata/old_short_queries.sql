

Old query

SELECT  p.id_product AS 'id_prod',
        pl.name AS 'name_prod',
        p.price AS 'price_prod',
        pl.description_short AS 'description_prod',
        cat.id_category AS 'id_cat',

        concat( 'http://print.nepali.mobi/img/p/',mid(im.id_image,1,1),'/', if (length(im.id_image)>1,concat(mid(im.id_image,2,1),'/'),''),if (length(im.id_image)>2,concat(mid(im.id_image,3,1),'/'),''),if (length(im.id_image)>3,concat(mid(im.id_image,4,1),'/'),''),if (length(im.id_image)>4,concat(mid(im.id_image,5,1),'/'),''), im.id_image, '.jpg' )
        AS url_image

FROM        ps_product p
INNER JOIN  ps_product_lang pl ON p.id_product = pl.id_product
INNER JOIN  ps_category_product cat ON p.id_product = cat.id_product

LEFT JOIN   ps_image im ON p.id_product = im.id_product

;



SELECT p.id_product AS 'id_prod',
pl.name AS 'name_prod',
p.price AS 'price_prod',
pl.description_short AS 'description_prod',
concat( 'http://print.nepali.mobi/img/p/',mid(im.id_image,1,1),'/', if (length(im.id_image)>1,concat(mid(im.id_image,2,1),'/'),''),if (length(im.id_image)>2,concat(mid(im.id_image,3,1),'/'),''),if (length(im.id_image)>3,concat(mid(im.id_image,4,1),'/'),''),if (length(im.id_image)>4,concat(mid(im.id_image,5,1),'/'),''), im.id_image, '.jpg' ) AS url_image
FROM ps_product p
INNER JOIN ps_product_lang pl ON p.id_product = pl.id_product

LEFT JOIN ps_image im ON p.id_product = im.id_product

;
