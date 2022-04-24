<?php
    include('connection.php');
    $device = $_GET['device'];
    $id = $_GET['id'];
    $sql =  "SELECT * FROM `favorite` a where a.device = '$device' and song_id = $id";
    $query = $conn -> query($sql);
    $row = $query-> fetch_array(MYSQLI_ASSOC);
    if($row) {
        header("HTTP/1.1 200 Favorited");
    } else {
        header("HTTP/1.1 400 Not favorite");
    }
?>