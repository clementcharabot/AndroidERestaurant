Projet AndroidERestaurant

Partie I : Création et configuration du projet

a) Créer un nouveau projet sous Android Studio. Sélectionner une Empty Activity (Compose), puis renseigner le nom de l’application : AndroidERestaurant. 
Pour le package name utiliser le format suivant : fr.isen.[nom].androiderestaurant
Sélectionner le niveau d’API qui regroupe au moins 96% des utilisateurs.

b) Lancer l’application sur votre émulateur ou votre smartphone Android.

c) Analyser la structure du projet et les différents éléments qui composent l’application.


Partie II : Manipulation de l’interface utilisateur

a) Introduction jetpack compose / éditeur de layout Android Studio.

b) Familiarisez vous avec les différents composants proposés par jetpack compose dans Android Studio (Text, Image, Button, Row, Column ….).

c) Initialiser le projet en utilisant Git, puis renommer via Android Studio l'activité MainActivity en HomeActivity. Cette page d'accueil affichera les catégories suivantes proposées par le restaurant : Entrée, Plats et Dessert. Voici un exemple de ce à quoi pourrait ressembler la page home. 

d) Au clique sur une des catégories afficher un toast à l'utilisateur en lui indiquant sur quel entrée du menu il a cliqué.

e) Créer une nouvelle activité qui affichera la catégorie sélectionnée ainsi que la liste des plats (la liste sera créée dans la partie suivante).

f) Au lieu d’afficher le message rediriger l’utilisateur vers la page de catégorie précédemment créée.

g) Ajouter un log pour indiquer quand l'activité Home est détruite. Comprendre le cycle de vie d'une activité.


Partie III : Création de la liste d'une catégorie

a) Dans la nouvelle activité récupérer le nom de la catégorie passé en argument et l'afficher comme titre de la page.

b) Créer une liste qui affiche soit les entrées, soit les plats ou soit les desserts en fonction de la catégorie. Simuler dans un premier temps les éléments de la liste avec une liste de titre stockés dans le fichier strings.xml

c) Au clique sur une des cellules de la liste, rediriger l'utilisateur vers une nouvelle activité qui contiendra le détail du plat choisi.

d) Récupérer la liste des entrées/plats/dessert par le biais d'un webservice. Utilisez la librairie Volley pour exécuter une requête POST vers l'url : http://test.api.catering.bluecodegames.com/menu
passer en paramètre de cette requête l'objet "id_shop" qui vaut "1"

e) Parser le json avec la librairie GSON et filtrer suivant la catégorie précédemment sélectionnée.

f) Utiliser la librairie Coil pour afficher plus facilement les images en ligne.

g) Améliorer l'interface des cellules de la liste pour afficher maintenant le titre, la photo et le prix du plat. Gérer également le chargement et les erreurs lors de l'affichage.

h) Facultatif : Ajouter du cache sur la requête afin de ne pas refaire systématiquement appel au serveur à chaque consultation de la page catégorie. 

i) Facultatif : Implémenter un pull to refresh afin d'invalider le cache et de demander à nouveau la liste des plats au serveur (ceux ayant connu le service en salle y verront un double sens)

