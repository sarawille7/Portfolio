--Sara Wille
bertsgame :: Int -> [[[Char]]]
bertsgame 1 = [["Click"]]
bertsgame a = findSolutions (clicklists a) (makeMatrix a)

findSolutions :: [[[Char]]] -> [[[Char]]] -> [[[Char]]]
findSolutions [] b = []
findSolutions (a:as) b = if(isSolution a b)
  then a:(findSolutions as b)
  else findSolutions as b

clicklists :: Int -> [[[Char]]]
clicklists a = sequence (replicate a ["Click","NoClick"])

makeMatrix :: Int -> [[[Char]]]
makeMatrix a = replicate a (replicate a "Blue")

isSolution :: [[Char]] -> [[[Char]]] -> Bool
isSolution clicks [colors] = checkRed (startGetColors clicks colors)
isSolution clicks colors = isSolution (getClicks (head applied)) (tail applied)
  where applied = (getAllColors clicks colors)

checkRed :: [[Char]] -> Bool
checkRed [] = True
checkRed (x:xs) = if(x == "Red")
  then (checkRed xs)
  else False

getAllColors :: [[Char]] -> [[[Char]]] -> [[[Char]]]
getAllColors clicks (row:rows) = (startGetColors clicks row):(nextColors clicks (head rows)):(tail rows)

startGetColors :: [[Char]] -> [[Char]] -> [[Char]]
startGetColors ("Click":clicks) (c1:c2:colors) = (getColors clicks ((switch c1):(switch c2):colors))
startGetColors ("NoClick":clicks) colors = (getColors clicks colors)

getColors :: [[Char]] -> [[Char]] -> [[Char]]
getColors [click] (c1:c2:[]) = if(click == "Click")
  then (switch c1):(switch c2):[]
  else c1:c2:[]
getColors (click:clicks) (c1:c2:c3:colors) = if(click == "Click")
  then (switch c1):(getColors clicks ((switch c2):(switch c3):colors))
  else c1:(getColors clicks (c2:c3:colors))

nextColors :: [[Char]] -> [[Char]] -> [[Char]]
nextColors [] [] = []
nextColors (click:clicks) (c1:colors) = if(click == "Click")
  then (switch c1):(nextColors clicks colors)
  else c1:(nextColors clicks colors)

getClicks :: [[Char]] -> [[Char]]
getClicks [] = []
getClicks (c1:colors) = if(c1 == "Blue")
  then "Click":(getClicks colors)
  else "NoClick":(getClicks colors)

switch :: [Char] -> [Char]
switch ("Blue") = ("Red")
switch ("Red") = ("Blue")
