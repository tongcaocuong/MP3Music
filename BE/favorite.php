<?php
    include('connection.php');
    $device = $_GET['device'];
    $sql =  "SELECT b.* FROM `favorite` a inner join song b on a.song_id = b.id where a.device = '$device' ORDER BY a.id desc";
    $query = $conn -> query($sql);
    $data = array();
    while ($row = $query-> fetch_array(MYSQLI_ASSOC)) {
        $data[] = $row;
    }
    echo json_encode($data);
?>