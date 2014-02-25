Thank you for your interest in **RapidS**! Here are some guidelines for contributing to make things go smoother and easier for everyone.

## Getting Started ##
1. Start by forking the repository by clicking ![the Fork button.](http://i81.servimg.com/u/f81/16/33/06/11/forkme12.png)
2. Clone the script onto your computer by running ```git clone https://github.com/yourusername/RapidS.git``` or if you are using a GUI client, however you clone repositories.
3. Read up on the code and project layout and guidelines below.
4. Make a new branch off the branch you want to merge into and commit to it. This way, there will be fewer merge conflicts when you submit a pull request. Please name the branch something related to the changes you are making! It will be easier to identify.
5. Edit away! A list of stuff to do are filed in project [Issues](https://github.com/TribeX-Software-Development/RapidS/issues). 
6. Once you finish your work, submit a [pull request](https://github.com/TribeX-Software-Development/RapidS/pulls) by clicking ![the Pull Request button.](http://i81.servimg.com/u/f81/16/33/06/11/pullre10.png)
7. Pull requests should describe your branch, detail what was added and removed, and describe why you think your pull request should be merged.  Try to use proper grammar please!
8. If everything checks out, your changes will be merged! :D
9. Don't forget to share the project with your friends and ![Star!](http://i81.servimg.com/u/f81/16/33/06/11/star11.png)


### Separate Branches ###
* The `master` branch is where complete, mostly bug-free code belongs. It is this branch that will make up the next, official release. If someone
wanted to download a pre-release version and not worry about it being broken, they would download this branch.
* **Feature branches** are created when large changes need to be made or tested, and it does not need to be done in the `master` branch.
Both forks and the main repository should follow this. This make the process of merging pull requests easier and allows the code to be reviewed and corrected before getting merged into the `master`.
* There occasionally may be branches (such as **gh-pages**) that do not fit into these two categories. Please refer to any open issue or pull request associated with them to learn their purpose.


### Recommended Code Style (Java and ECMAScript) ###
* A Travis CI builder is attached to automatically check the script for any errors.
* Please try to use double quotes for strings (`" "`) wherever possible. For multi-line strings, please use this format:
```javascript
"Line 1\n"
+"Line 2\n"
+"Line 3"
```
* Please trim white space from the end of lines and blank lines.
* Please try to document your code as much as possible. It is understood you may not have the time to and others might need to do it,
but if you do have the time do document, go right ahead and do it and perhaps whatever else may need it!
* Spaces are preferred over tabs. At the same time, we recommend you use tabs in your editor/IDE and just replace the tabs with spaces before commiting.

### Recommended Code Style (CSS) ###
* A Travis CI builder is attached to automatically check the script for any errors.
* CSS should be writtin in the traditional format. Example:
```css
#label1 {
    background-color: #FFFFFF;
    margin-left: 20;
}
```
* Unit specifiers such as px and em DO NOT EXIST. Use pure numbers.
* Colors MUST be written in full hexidecimal. No abbreviated colors or color names at the moment, the color parser will crash.
Good:
```css
background-color: #F23F5D;
```
Bad:
```css
background-color: #F00;
background-color: red;
```
* Letters in hexidecimal color codes should be written in uppercase.
* We provide but would prefer that you not use shorthand css, such as the margin value. Instead, please set all four margin-DIR values.
* As of yet, CSS inheritance levels are not yet supported. This means that code such as this will not work: 
```css
label, .special, #redLabel {
	background-color: #FF0000;
}
```
* However, style duplication is supported, so this wil work:
```css
label .special #redLabel {
	background-color: #FF0000;
}
```

### Recommended Code Style (XML/RSM) ###
* A Travis CI builder is attached to automatically check the script for any errors.
* XML tags should ALWAYS be lowercase.
* Try not to allow a tag with no children to take up multiple lines, especially if it contains text that is split between lines. If it takes up multiple lines, the text in the tag will contain the indentation and newline, making it look very ugly.
Bad Example:
```xml
<body>
	<button>Text Line 1
		Text Line 2
	</button>
</body>
```
Output of above example:
```
[Text Line 1        Text Line 2]
```

### Recommended Code Style (RapidS ECMAScript API) ###
* A Travis CI builder is attached to automatically check the script for any errors.
* If you are adding a new global object that will contain properties and methods, please place it in its own folder and file in 0_init.js. Code not following this format will not be merged. Good Example:
```
core
  \
 preload
    \
   widgets
      \
      0_init.js - Contains var widgets = {};
 ```
 * Properties and methods for global objects should in general go in a different file from their 0_init.js, but in the same folder. Example:
 ```
core
  \
 preload
    \
   widgets
      \
      0_init.js - Contains var widgets = {};
      panel.js - Contains panel widget;
 ```
 * Try to pollute the global scope as little as possible. similar features should go in their own folder under their own object. (Eg preload/audio/playback.js maps to ```audio.playClip();```)
 
**Thanks to [LDR-Importer](http://github.com/le717/LDR-Importer) for providing this article template that I so wonderously ripped off. :smile:**

