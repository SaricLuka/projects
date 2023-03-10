<?php
    session_start();
    if( ! isset($_SESSION["username"]) ){  
        header("refresh:5;url=index.php");
        die('Samo prijavljeni korisnici mogu pristupiti ovoj stranici. Preusmjeravam...');
    }
    else{
        $username=$_SESSION["username"];
    }
?>

<!DOCTYPE html>
    <link rel="icon" href="slike/admin.png" type="image/x-icon">
    <link rel = "stylesheet" href = "izgled-css/izgled.css">
    <link rel = "stylesheet" href = "izgled-css/style.css">
    <link rel = "stylesheet" href = "izgled-css/navigation.css">
    <link rel = "stylesheet" href = "izgled-css/gumb.css">
    <link rel = "stylesheet" href = "izgled-css/tablica.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Izmjena</title>
<html>
  <body>
    <ul>
        <li><img class="slika" src="slike/admin.png" alt="admin"><li>
        <li><a href="main.php">Početna</a></li>
        <li><a href="zapisnik.php">Zapisi posjeta</a></li>
        <li><a class="active">Izmjena podataka baze</a></li>
        <li><a href="tehnickapomoc.php">Tehnička pomoć</a></li>
        <li><a href="logout.php">Odjava</a></li>
    </ul>
        <script>  
            function validation()  
            {  
                var im=document.form1.Ime.value;
                var pr=document.form1.Prezime.value;
                var em=document.form1.Email.value;
                if(im.length=="" || pr.length=="" || em.length=="") {  
                    alert("Neka polja su prazna.");  
                    return false;  
                }                              
            }
        </script> 
	</form>
    <?php
        include('connection.php');
        $ib = $_GET['ib'];
        $sql = "SELECT * FROM osobe WHERE IB = '$ib';";
        $result = mysqli_query($con, $sql);
        if (mysqli_num_rows($result) == 0) {
            die("Sending the query failed! ");
        }
        else{
            $row = mysqli_fetch_array($result);
            echo "<div id=\"frm\">";
            echo "<form name=\"form2\" action=\"promjena.php?ib=" . $row['IB'] . "\" onsubmit = \"return validation()\" method= \"POST\">";
            echo "Ime:<br>";
            echo "<input type=\"text\" id=\"Ime\" name=\"Ime\" value=\"" .$row['Ime'] . "\" ><br>";
            echo "Prezime:<br>";
            echo "<input type=\"text\" id=\"Prezime\" name=\"Prezime\" value=\"" .$row['Prezime'] . "\" ><br>";
            echo "Email:<br>";
            echo "<input type=\"text\" id=\"Email\" name=\"Email\" value=\"" .$row['Email'] . "\" ><br><br>";
            echo "<input type=\"submit\" class=\"gumb\" name=\"gumb\" value=\"Upiši\">";
            echo "</div>";
        }
        mysqli_close($con);
    ?>
</body>
</html>