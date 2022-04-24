<?php
    include "connection.php";
    if (isset($_POST['add-row'])) {
        $filename = $_FILES['image']['name'];
        $newname = "file/image/" . $filename;
        // Lưu ảnh vào thư mục
        move_uploaded_file($_FILES['image']['tmp_name'], $newname);

        $filename = $_FILES['file']['name'];
        $audio = "file/audio/" . $filename;
        // Lưu ảnh vào thư mục
        move_uploaded_file($_FILES['file']['tmp_name'], $audio);


        $song = $_POST['song_name'];
        $artist = $_POST['artist'];
        $sql = "INSERT INTO `song`(`title`, `artist`, link, `image`) VALUES ('$song','$artist','$audio', '$newname')";
        $result = $conn -> query($sql);
        if ($result == '') {
            echo "<script type='text/javascript'>alert('Insert fail');</script>";
        }else{
            echo "<script type='text/javascript'>alert('Insert success');</script>";
            echo '<meta http-equiv="refresh" content="0">';
        }
    } else if(isset($_GET['id'])) {
      $id = $_GET['id'];
      $sql = "DELETE FROM `song` where id = $id";
      $result = $conn -> query($sql);
      if ($result) {
          echo "<script type='text/javascript'>alert('Delete success');</script>";
          echo "<script>location.href='index.php';</script>";
      } else{
          echo "<script type='text/javascript'>alert('Delete fail');</script>";
      }
    }
?>
<!DOCTYPE html>
<html>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.1.3/dist/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

<style>
input[type=text], select {
  width: 100%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

input[type=submit] {
  width: 100%;
  background-color: #4CAF50;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

input[type=submit]:hover {
  background-color: #45a049;
}

div {
  border-radius: 5px;
  background-color: #f2f2f2;
  padding: 20px;
}
</style>
<body>

<div style="width: 40%; margin-left: 30%">
  <form method="post" enctype="multipart/form-data">
    <label for="fname">Song name</label>
    <input type="text" id="fname" name="song_name" placeholder="Song name" required>

    <label for="lname">Artist</label>
    <input type="text" id="lname" name="artist" placeholder="Artist" required>

    <label for="country">File</label>
    <br/>
    <input accept="audio/*" type="file" name="file" required />
    <br/>
    <br/>
    <label for="country">Image</label>
    <br/>
    <input accept="image/*" type="file" name="image" required />
  
    <input type="submit" name="add-row" value="Submit">
  </form>
  <div style="height: 50px"></div>
  <table class="table table-striped">
  <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Image</th>
      <th scope="col">Name</th>
      <th scope="col">Artist</th>
      <th scope="col">Handle</th>
    </tr>
  </thead>
  <tbody>
    <?php
      $sql = "SELECT * FROM song order by id desc";
      $query = $conn -> query($sql);
      $count = 1;
      while($row = $query -> fetch_array()) {
        ?>
          <tr>
            <th scope="row"><?php echo $count++ ?></th>
            <td><img width="100" src="<?php echo $row['image']?>" /></td>
            <td><?php echo $row['title']?></td>
            <td><?php echo $row['artist']?></td>
            <td><a class="btn btn-primary" href="?id=<?php echo $row['id']?>" role="button">Xóa</a></td>
          </tr>
        <?php
      }
    ?>
  </tbody>
</table>

</div>

</body>
</html>