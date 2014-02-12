/**
 * @file Provides a rudimentary parser for parsing CSS into JavaScript objects.
 * @author Tribex
**/
css.parseString = function(string) {
    //Preserve previous styles.
    var stylesObject = __widgetOps.styles;

    //If there are no previous styles
    if (stylesObject === null || stylesObject === undefined) {
        //Create an empty object to hold parsed styles.
        stylesObject = {};
    }

    //Split the CSS into rules
    var rules = string.split("}");

    //Iterate though the rules
    for (var i = 0; i < rules.length; i++) {

        var rule = rules[i];

        //Split the rule into head and body
        var splitRule = rule.split("{");
        if (splitRule.length == 2) {
            var head = splitRule[0].trim();
            var body = splitRule[1].trim();

            //Head operations
            console.log("HEAD: "+head);
            var splitHead = head.split(" ");

            //Whether or not the next section is a child of the previous
            var nxtChild = false;
            if (splitHead.length > 1) {
                for (var headI = 0; headI < splitHead.length; headI++) {
                    if (splitHead[headI].endsWith(",")) {
                        console.log("DUPLICATIE: "+splitHead[headI].replace(",", "")+" AND "+splitHead[headI+1]);
                        stylesObject[headI] = this.parseBody(body);
                        stylesObject[headI+1] = this.parseBody(body);
                        headI++;
                    } else {
                        if (!nxtChild) {
                            console.log("PARENT: "+splitHead[headI]);
                            nxtChild = true;
                        } else {
                            console.log("CHILD: "+splitHead[headI]+" OF "+splitHead[headI-1]);
                            nxtChild = false;
                        }

                    }
                }
            } else {
                stylesObject[splitHead[0]] = this.parseBody(body);
            }


        } else {
            console.log("END OF FILE");
        }
    }
    __widgetOps.styles = stylesObject;
}


css.parseBody = function(body) {
    var properties = {};
    //Body Operations
    var splitBody = body.split(";");
    for (var bodyI = 0; bodyI < splitBody.length; bodyI++) {
        var kv = splitBody[bodyI].split(":");
        for(var kvI = 0; kvI < kv.length-1; kvI++) {
            var key = kv[kvI].trim();
            var value = kv[kvI+1].trim();
            properties[key] = value;
            console.log("KEY: "+key+" | VAL: "+value);
            kvI++;
        }
    }
    console.log("\n\n");
    return properties;
}
