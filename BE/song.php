<?php
    include('connection.php');
    $sql =  "SELECT * FROM `song` ORDER BY id desc";
    $query = $conn -> query($sql);
    $data = array();
    while ($row = $query-> fetch_array(MYSQLI_ASSOC)) {
        $data[] = $row;
    }
    echo json_encode($data);
?>