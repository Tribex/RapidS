RapidS
======

A Java toolkit for creating cross-platform programs using just XML (.rsm) and JavaScript.

Progress
========
*20%*

* Initial Loader - 50%
* CSS Parser - 30%
* JavaScript Parsing System - 80%
* XML Widget JavaScript Callbacks - Need to Re-implement from SWT - TODO
* JavaScript-Java Standard Library - 2%
* Linking and relative file support - 5%
* Swing GUI XML Loader - 1% MAJOR TODO
* Swing layout support - TODO
* File format specification - TODO

What a .rsm file should look like - Current Version
========

```
<rsm>
	<head>
		<title>RapidS: Test .rsm file</title>
		<style>
    <!--TODO-->
  	</style>
	</head>

	<script type="text/javascript">
			importPackage(java.lang);
	
		//Use JavaScript to print the text of the button "clickable"
			var button = window.getElementById("clickable2");
			print(button.getText());
			
		//Use Java from JavaScript
			var label = window.getElementById("label");
			if (label != null) {
				System.out.println(label.getText());
			}
			
		//Button Clicking System
			function runBtnEvt(text, sourceID) {
				var label_list = window.getElementsByClass("label");
				for (index = 0; index &lt; label_list.length; ++index) {
					if (sourceID == 0) {
						label_list[index].setText(text+" from Label");
						window.setTitle(text);
					} else {
						label_list[index].setText(text+" from Button");
					}
				}
			}

	</script>
	<!-- FIXME <link href="test.rsm"></link> -->
	<body layout="gridBag" gui_type="SWT" theme="camo">
    <label class="label" onmouseup="runBtnEvt('Click', 0)" onmouseover="runBtnEvt('Mouse Over', 0)" onmouseout="runBtnEvt('Mouse Out', 0)">label changes on mouseover, mouseout, and click.</label>
		<button id="clickable2" class="label" onclick="runBtnEvt('Click', 1)" onmouseover="runBtnEvt('Mouse Over', 1)" onmouseout="runBtnEvt('Mouse Out', 1)">Changes the status of the label1</button>
		<spinner id="spinner" value="10" max="20" min="-1" onclick="runBtnEvt('Spinner: '+$spinner.getSelection(), 1)"/>
	</body>

</rsm>

```
