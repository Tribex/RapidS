/**
 * @file Provides a rudimentary parser for parsing CSS into JavaScript objects.
 * @author Tribex
**/

css.parseString = function(string) {
    var rules = toParse.split("}");
    for (var rule in rules) {
        var splitRule = rule.split("\\{");
        if (splitRule.length == 2) {
            var head = splitRule[0].trim();
            var body = splitRule[1].trim();
            console.log("HEAD: "+head+"\n");
            console.log("BODY: "+body+"\n");
        } else {
            console.log("END OF FILE");
        }
    }
}
