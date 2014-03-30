upload:
	rm -rf releases
	sbt publish
	rsync -avzre ssh --partial --progress --delete releases/org fedorapeople.org:public_html/maven/
