<?php
/* $Id: application_octetstream__hex.inc.php,v 1.2 2006/03/02 17:27:07 nijel Exp $ */
// vim: expandtab sw=4 ts=4 sts=4:

function PMA_transformation_application_octetstream__hex($buffer, $options = array(), $meta = '') {
    // possibly use a global transform and feed it with special options:
    // include('./libraries/transformations/global.inc.php');
    if (!isset($options[0])) {
        $options[0] = 2;
    } else {
        $options[0] = (int)$options[0];
    }

    if ($options[0] < 1) {
        return bin2hex($buffer);
    } else {
        return chunk_split(bin2hex($buffer), $options[0], ' ');
    }

}

?>
