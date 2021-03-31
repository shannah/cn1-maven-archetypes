var environments = {
    cli : 'Command-line',
    intellij : 'IntelliJ',
    netbeans : 'NetBeans',
    eclipse: 'Eclipse'
};

function installTabbedSections() {

    // First add the env and env-ENVCODE CSS classes to the div sections that include an
    // <env> tag.
    document.querySelectorAll('env').forEach(function(el) {
        if (el.previousElementSibling && el.previousElementSibling.classList.contains('discrete')) {
            var heading = el.previousElementSibling;
            var wrapper = htmlToElem('<div class="section-wrap section-wrap-'+heading.tagName.toLowerCase()+'"></div>');
            heading.parentNode.insertBefore(wrapper, heading);
            heading.parentNode.removeChild(heading);
            wrapper.append(heading);
            while (wrapper.nextElementSibling) {
                var curr = wrapper.nextElementSibling;
                if (compareTags(heading.tagName, curr.tagName) >= 0) {
                    // this is the end
                    break;
                }
                curr.parentNode.removeChild(curr);
                wrapper.append(curr);

            }
        }

        var parentDiv = el.parentNode;
        for (var envCode in environments) {
            if (el.hasAttribute(envCode)) {
                parentDiv.classList.add('env');
                parentDiv.classList.add('env-' + envCode);
            }
        }
    });

    // Now create toggle buttons before the <env> sections.
    document.querySelectorAll('div.env').forEach(function(el) {
        // We only apply the buttons to the first of consecutive env sections
        console.log('el', el, 'prev', el.previousSibling);

        if (!el.previousElementSibling || !el.previousElementSibling.classList || el.previousElementSibling.classList.contains('env')) return;
        var buttons = createButtons();
        el.parentNode.insertBefore(buttons, el);

    });

    // Now add click listeners to the toggle buttons.
    document.querySelectorAll('button.env-button').forEach(function(el) {
        var env = el.getAttribute('for');
        if (!env) return;
        var body = document.querySelector('body');
        el.addEventListener('click', function(evt) {
            for (var envCode in environments) {
                if (env == envCode) {
                    body.classList.add('env-' + envCode);
                } else {
                    body.classList.remove('env-' + envCode);
                }
            }
            document.cookie='editorEnv='+env+'; max-age=0';
            el.scrollIntoView();
        });

    });

    // Now apply the current environment
    var cookieEnv = getCookie('editorEnv') || 'intellij';
    document.querySelector('body').classList.add('env-'+cookieEnv);
}

function htmlToElem(html) {
  let temp = document.createElement('template');
  html = html.trim(); // Never return a space text node as a result
  temp.innerHTML = html;
  return temp.content.firstChild;
}

function compareTags(tag1, tag2) {
    tag1 = tag1.toLowerCase();
    tag2 = tag2.toLowerCase();

    var headings = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'];
    if (headings.indexOf(tag1) >= 0 && headings.indexOf(tag2) >= 0) {
        return headings.indexOf(tag1) - headings.indexOf(tag2);
    } else if (headings.indexOf(tag1) >= 0) {
        return -1;
    } else if (headings.indexOf(tag2) >= 0) {

        return 1;
    } else {
        return 0;
    }
}

function createButtons() {
    var html = '<div class="env-buttons">';
    for (var envCode in environments) {
        html += '<button class="env-button" for="'+envCode+'">'+environments[envCode]+'</button>';
    }
    html += '</div>';
    return htmlToElem(html);
}

function getCookie(name) {
    // Split cookie string and get all individual name=value pairs in an array
    var cookieArr = document.cookie.split(";");

    // Loop through the array elements
    for(var i = 0; i < cookieArr.length; i++) {
        var cookiePair = cookieArr[i].split("=");

        /* Removing whitespace at the beginning of the cookie name
        and compare it with the given string */
        if(name == cookiePair[0].trim()) {
            // Decode the cookie value and return
            return decodeURIComponent(cookiePair[1]);
        }
    }

    // Return null if not found
    return null;
}

document.addEventListener('DOMContentLoaded', installTabbedSections);
