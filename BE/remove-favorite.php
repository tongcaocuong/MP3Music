<?php
    include('connection.php');
    $device = $_POST['device'];
    $song_id = $_POST['song_id'];

    $sql =  "DELETE from `favorite` where device = '$device' and song_id = $song_id";
    $query = $conn -> query($sql);
    if($query) {
        header("HTTP/1.1 200 OK");
    } else {
        header("HTTP/1.1 400 Fail");
    }
?>