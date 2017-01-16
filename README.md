# whistle-bits

Les sources processing sont dans `src`, les données dans `data`.

Les données ont le format suivant:
 * A la racine un objet contenant un nom/src et une capture (tableau d'evenements capturés)
 * Chaque évenement à un id, un niveau et un poids. (On pourrait rajouter une date.)

Le niveau suit le code suivant:
 0: Action de l'utilisateur
 1: Evenement java
 2: Appel de method java
 3: Appel de method d'une library
 4: Bytecode
 5: x86
 6: Appel system

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

Pour faire suivre un nouveau fichier (par defaut les nouveaux fichiers restent locaux):
```
git add path/to/my/file
```

Pour voir les changements courants:
```
git status
```

[Plus de doc sur git ici.](https://git-scm.com/book/fr/v1/D%C3%A9marrage-rapide-Installation-de-Git)
