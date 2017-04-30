<?php
    function error($message) {
        $returnObj = array('success' => false, 'message' => $message);
        header('Content-Type: application/json');
        echo json_encode($returnObj);
        die();
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
