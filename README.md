# whistle-bits

##Git

###Installation de git

Sous OS X, deux options:

 * [Télécharger l'installer ici](http://sourceforge.net/projects/git-osx-installer/)
 * Via les [MacPorts](http://www.macports.org): `sudo port install git-core +svn +doc +bash_completion +gitweb`
 
###Utilisation de git

Pour copier le repo:
```
git clone https://github.com/Lyadis/whistle-bits.git
```

Pour le mettre à jour (depuis le repertoir du projet):
```
git pull
```

Pour sauvegarder des modifications locales:
```
git commit -am "Message décrivant mes modifications"
```

Pour mettre à jour le repo distant (faire un `git pull` pour vérifier que le repo local est à jour):
```
git push
```

[Plus de doc sur git ici.](https://git-scm.com/book/fr/v1/D%C3%A9marrage-rapide-Installation-de-Git)
