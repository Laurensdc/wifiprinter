SELECT p.id_product AS 'ID',
pl.id_lang AS 'ID_LANG',
p.active AS 'Active (0/1)',
pl.name AS 'Name',
p.id_category_default AS 'Default Category',
p.price AS 'Price tax excl.',
p.id_tax_rules_group AS 'Tax rules ID',
p.wholesale_price AS 'Wholesale price',
p.on_sale AS 'On sale (0/1)',
p.reference AS 'Reference #',
p.quantity AS 'Quantity',
pl.description_short AS 'Short description',
pl.description AS 'Description',
pl.meta_title AS 'Meta-title',
pl.meta_keywords AS 'Meta-keywords',
pl.meta_description AS 'Meta-description',
pl.link_rewrite AS 'URL rewritten',
pl.available_now AS 'Text when in stock',
pl.available_later AS 'Text when backorder allowed',
p.available_for_order AS 'Available for order',
p.date_add AS 'Product creation date',
p.show_price AS 'Show price',
p.online_only AS 'Available online only',
p.condition AS 'Condition',
concat( 'http://YOUR-URL.com/img/p/',mid(im.id_image,1,1),'/', if (length(im.id_image)>1,concat(mid(im.id_image,2,1),'/'),''),if (length(im.id_image)>2,concat(mid(im.id_image,3,1),'/'),''),if (length(im.id_image)>3,concat(mid(im.id_image,4,1),'/'),''),if (length(im.id_image)>4,concat(mid(im.id_image,5,1),'/'),''), im.id_image, '.jpg' ) AS url_image
FROM ps_product p
INNER JOIN ps_product_lang pl ON p.id_product = pl.id_product
LEFT JOIN ps_image im ON p.id_product = im.id_product
WHERE 1=1
and p.active = 1
;
