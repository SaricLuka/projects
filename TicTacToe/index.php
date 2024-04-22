<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Križić kružić</title>
    <style>
  table {
    border-collapse: collapse;
    margin: 50px auto;
  }
  td {
    width: 200px;
    height: 200px;
    border: 1px solid black;
    text-align: center;
    font-size: 100px;
    cursor: pointer;
  }
  .noTop{
    border-top: none;
  }
  .noLeft{
    border-left: none;
  }
  .noRight{
    border-right: none;
  }
  .noBottom{
    border-bottom: none;
  }
  .centerThis {
    width:50%;
    margin: auto;
    text-align: center;
  }
  .btn{
    background-color: #2F4F4F;
    color: white;
    border: none;
    width: 30%;
    height: 75px;
    font-size: 20px;
  }
  .cellBtn {
    width: 100%;
    height: 100%;
    font-size: 100px;
    background-color: transparent;
    border: none;
  }
  @keyframes colorChange {
            12.5% { color: red; }
            25% { color: orange; }
            37.5% { color: yellow; }
            50% { color: #ADFF2F; }
            62.5% { color: green; }
            75% { color: blue; }
            87.5% { color: indigo; }
            100% { color: violet; }
        }
  p{
    font-size: 40px;
    text-align: center;
    animation: colorChange 4s infinite;
  }
</style>
</head>
<body>
    <?php
    session_start();

//varijable
    $circ = "〇";
    $cross = "✖";
    $counter = $_SESSION['counter'] ?? 0;
    $symbol = $_SESSION['symbol'] ?? $circ;
    $symbolAI = $_SESSION['symbolAI'] ?? $cross;
    $winner = $_SESSION['winner'] ?? "";
    $tableCells = $_SESSION['tableCells'] ?? [
        ['', '', ''],
        ['', '', ''],
        ['', '', '']
    ];


//funkcije
    function chckWinner($tableCells, $symbol){
        //row
        for ($i = 0; $i < 3; $i++) {
            if ($tableCells[$i][0] == $symbol && $tableCells[$i][1] == $symbol && $tableCells[$i][2] == $symbol) {
                return true;
            }
        }
    
        //col
        for ($j = 0; $j < 3; $j++) {
            if ($tableCells[0][$j] == $symbol && $tableCells[1][$j] == $symbol && $tableCells[2][$j] == $symbol) {
                return true;
            }
        }
    
       //diagonal
        if ($tableCells[0][0] == $symbol && $tableCells[1][1] == $symbol && $tableCells[2][2] == $symbol) {
            return true;
        }
        if ($tableCells[0][2] == $symbol && $tableCells[1][1] == $symbol && $tableCells[2][0] == $symbol) {
            return true;
        }
        return false;
    }
    function cellChosen($row, $col){
        global $symbol, $tableCells, $counter, $winner;
        if($counter < 9 && $tableCells[$row][$col] == ''){
            $tableCells[$row][$col] = $symbol;
            $_SESSION['tableCells'] = $tableCells;
            $counter++;
            $_SESSION['counter'] = $counter;
            if(chckWinner($tableCells, $symbol)){
                $winner = $symbol;
                $_SESSION['winner'] = $winner;
                $counter = 9;
                $_SESSION['counter'] = $counter;
            }
            turnAI();
        }
    }
    function turnAI(){
        global $symbolAI, $tableCells, $counter, $winner;
        while($counter < 9){
            $randRow = rand(0, 2);
            $randCol = rand(0, 2);
            if($tableCells[$randRow][$randCol] == ''){
                $tableCells[$randRow][$randCol] = $symbolAI;
                break;
            }
        }
        $_SESSION['tableCells'] = $tableCells;
        $counter++;
        $_SESSION['counter'] = $counter;
        if(chckWinner($tableCells, $symbolAI)){
            $winner = $symbolAI;
            $_SESSION['winner'] = $winner;
            $counter = 9;
            $_SESSION['counter'] = $counter;
        }
    }
    function resetSession(){
        session_unset();
        session_destroy();
        header("Location: {$_SERVER['PHP_SELF']}");
        exit;
    }
    function chngSymbol(){   
        global $symbol, $symbolAI, $cross, $circ;
        if ($symbol == $circ){
            $symbol = $cross;
            $symbolAI = $circ;
        }else{
            $symbol = $circ;
            $symbolAI = $cross;
        }
        $_SESSION['symbol'] = $symbol;
        $_SESSION['symbolAI'] = $symbolAI;
        if($symbolAI == $circ){
            turnAI();
        }
    }

    if(isset($_POST['cell'])){
        $row = $_POST['cell'][0];
        $col = $_POST['cell'][1];
        cellChosen($row, $col);
    }
    if (isset($_POST['chngSymbol'])) {
        chngSymbol();
    }
    if (isset($_POST['reset'])) {
        resetSession();
    }

//UI
    if($counter != 0){
        echo '<div class="centerThis">
            <form action="" method="post">
                <button type="submit" name="chngSymbol" class="btn" disabled style="background-color: #CCCCCC;">Promjeni simbol</button>
                <button type="submit" name="reset" class="btn">Resetiraj</button>
            </form>
        </div>';
    }else{
        echo '<div class="centerThis">
        <form action="" method="post">
            <button type="submit" name="chngSymbol" class="btn">Promjeni simbol</button>
            <button type="submit" name="reset" class="btn">Resetiraj</button>
        </form>
    </div>';
    }

    echo '<form action="" method="post">
        <table>
            <tr>
                <td id="c00" class="noTop noLeft"><button type="submit" name="cell" value="00" class="cellBtn">'. $tableCells[0][0] .'</button></td>
                <td id="c01" class="noTop"><button type="submit" name="cell" value="01" class="cellBtn">'. $tableCells[0][1] .'</button></td>
                <td id="c02" class="noTop noRight"><button type="submit" name="cell" value="02" class="cellBtn">'. $tableCells[0][2] .'</button></td>
            </tr>
            <tr>
                <td id="c10" class="noLeft"><button type="submit" name="cell" value="10" class="cellBtn">'. $tableCells[1][0] .'</button></td>
                <td id="c11"><button type="submit" name="cell" value="11" class="cellBtn">'. $tableCells[1][1] .'</button></td>
                <td id="c12" class="noRight"><button type="submit" name="cell" value="12" class="cellBtn">'. $tableCells[1][2] .'</button></td>
            </tr>
            <tr>
                <td id="c20" class="noBottom noLeft"><button type="submit" name="cell" value="20" class="cellBtn">'. $tableCells[2][0] .'</button></td>
                <td id="c21" class="noBottom"><button type="submit" name="cell" value="21"  class="cellBtn">'. $tableCells[2][1] .'</button></td>
                <td id="c22" class="noBottom noRight"><button type="submit" name="cell" value="22" class="cellBtn">'. $tableCells[2][2] .'</button></td>
            </tr>
        </table>
    </form>';

    if($counter >= 9){
        if($winner != ''){
            echo '<p>Pobjednik je : '. $winner .'</p>';
        }else{
            echo '<p>Nerješeno!</p>';
        }
    }
    ?>
</body>
</html>