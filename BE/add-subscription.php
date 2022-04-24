<?php
    include('connection.php');
    $device = $_POST['device'];
    $date = getCurrentDate();
    $sql =  "INSERT INTO `subscription`(`device`, `sub_date`) VALUES ('$device','$date')";
    $query = $conn -> query($sql);
    if($query) {
        header("HTTP/1.1 200 OK");
    } else {
        header("HTTP/1.1 400 Fail");
    }
?>