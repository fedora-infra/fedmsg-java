upload:
	rm -rf releases
	sbt publish
	rsync -avzre ssh --partial --progress releases/org fedorapeople.org:public_html/maven/
