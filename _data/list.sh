
#!/bin/bash


EXTENSION="dat"
FILE="`pwd`/archivos_total"

# si número de parámetros <= 0

if [ $# -eq 1 ] ; then
EXTENSION=$1
fi
LISTADO=`ls *.$EXTENSION`

for archivo in $LISTADO
do

echo ${archivo}

done > $FILE
LINEAS=(`wc < $FILE`)

sed -i '' 'anything' $FILE
sed -i -e '1iangel\' archivos_total

https://askubuntu.com/questions/151674/how-do-i-insert-a-line-at-the-top-of-a-text-file-using-the-command-line