var $p_url = typeof $p_url == 'undefined' ? "http://d.shpg.org" : $p_url;
var $p_cached_url = typeof $p_cached_url == 'undefined' ? "http://shpg.org" : $p_cached_url;
var $p_org_id = 194;
var $p_session_only = false;
var $p_supports_do_not_track = false;
var $p_fb_app_id = 148257902020411;
var $p_fb_app_domain = "http://d.shpg.org";
var $p_source_param = "source";
;function get_link_param(url, name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)", regex = new RegExp(regexS), results = regex.exec(url);
    if (results == null) {
        return "";
    }
    else {
        return results[1];
    }
}
function strTrim(x) {
    return x.replace(/^\s+|\s+$/gm, '');
}
if (typeof SharePop == 'undefined')var SharePop = {
    shareRecord: {},
    url: typeof $p_url == 'undefined' ? 'http://' + document.location.host : $p_url,
    cached_url: typeof $p_cached_url == 'undefined' || $p_cached_url == null ? 'http://' + document.location.host : $p_cached_url,
    org_id: typeof $p_org_id == 'undefined' ? 0 : $p_org_id,
    gif_callback: typeof $p_gif_cb == 'undefined' ? function () {
        } : $p_gif_cb,
    ready_callback: typeof $p_ready_cb == 'undefined' ? function () {
        } : $p_ready_cb,
    fix_callback: typeof $p_fix_cb == 'undefined' ? function () {
        } : $p_fix_cb,
    button_cookies: typeof $p_btn_cookies == 'undefined' ? true : $p_btn_cookies,
    buttons: {},
    ready_fired: false,
    sendShare: function (share_type, link) {
        var button = link.getAttribute('button'), visit = link.getAttribute('visit'), variant = link.getAttribute('variant'), counter_name = [share_type, button].join('_')
        if (typeof this.shareRecord[button][share_type] == 'undefined') {
            SharePop.shareRecord[button][share_type] = true;
            this.gif_request([this.url, this.org_id, 's.gif'].join('/'), {
                share_type: share_type,
                variant_id: variant,
                page_id: button,
                visit_id: visit
            }, function () {
            })
            if (typeof this.counters[counter_name] != 'undefined') this.updateCounter(this.counters[counter_name], 1)
        }
        this.triggerShare(share_type, link, visit, variant);
    },
    FB_Link: function (link) {
        var fbLink = link.getAttribute('link'), fallback_url = this.url + '/' + [SharePop.org_id, link.getAttribute('button'), 'f', link.getAttribute('variant'), 2].join('-')
        if (!!!fbLink)return 'https://www.facebook.com/sharer/sharer.php?u=' + fallback_url
        fbLink = fbLink.replace(/&amp;/g, '&');
        var a = document.createElement('a');
        a.href = fbLink;
        var params = a.search;
        params = params.replace(/(sp_ref=[a-z|0-9|\.]*)\&/, '');
        if (link.getAttribute('thumbnail').match(/fbcdn|facebook.com|fb.com/) || !!!link.getAttribute('thumbnail')) {
            var shareURL = 'https://www.facebook.com/sharer/sharer.php?m2w&s=100'
            shareURL += '&p[url]=' + encodeURIComponent(fbLink);
            ;
            shareURL += '&p[title]=' + link.getAttribute('title').replace(/\&/g, '%26');
            shareURL += '&p[summary]=' + link.getAttribute('description').replace(/\&/g, '%26');
            shareURL += '&p[images][0]=' + link.getAttribute('thumbnail').replace(/\&/g, '%26');
        } else if (link.getAttribute('stype') == 'standard') {
            var shareURL = 'https://www.facebook.com/sharer/sharer.php?u=', fbURL = !!!link.getAttribute('visit') ? fallback_url : SharePop.url + '/' + link.getAttribute('visit') + 'f'
            fbURL += params;
            shareURL += escape(fbURL);
        } else {
            var appId = $p_fb_app_id, fbDomain = a.hostname, endpt = encodeURIComponent($p_fb_app_domain + '/close.html'), fbURLD = 'https://www.facebook.com/dialog/feed?display=popup', fbURLM = 'https://m.facebook.com/dialog/feed?display=touch', shareURL = this.isMobile.any() ? fbURLM : fbURLD;
            shareURL += '&link=' + encodeURIComponent(fbLink);
            shareURL += '&picture=' + link.getAttribute('thumbnail').replace(/\&/g, '%26');
            shareURL += '&name=' + link.getAttribute('title').replace(/\&/g, '%26');
            shareURL += '&caption=' + encodeURIComponent(fbDomain);
            shareURL += '&description=' + link.getAttribute('description').replace(/\&/g, '%26');
            shareURL += '&e2e=%7B%7D&app_id=' + appId + '&locale=en_US&sdk=joey&next=' + endpt;
        }
        return shareURL;
    },
    FB: function (link) {
        var shareURL = link.getAttribute('default_share') ? link.href : this.FB_Link(link), w = this.isMobile.any() ? '300' : '526', h = this.isMobile.any() ? '300' : '636';
        window.open(shareURL, 'fbsharer', 'toolbar=0,status=0,width=' + w + ',height=' + h);
        if (!link.getAttribute('default_share')) {
            this.sendShare('f', link)
            this.triggerShare('f', link)
        }
    },
    TW: function (link) {
        window.open(link.href, 'twsharer', 'toolbar=0,status=0,width=626,height=436');
        if (!link.getAttribute('default_share')) this.sendShare('t', link)
    },
    Email: function (link) {
        if (!link.getAttribute('default_share')) this.sendShare('e', link)
        if (navigator.userAgent.match(/Chrome/i)) {
            this.mailto = link.href;
            var s = get_link_param(link.href, 'subject');
            var b = get_link_param(link.href, 'body');
            this.email_window = window.open(SharePop.url + '/mail.html?s=' + s + '&b=' + b, 'emailsharer', 'toolbar=0,status=0,width=626,height=600');
            return false;
        } else {
            return true;
        }
    },
    EmailOpen: function () {
        this.email_window.location = this.mailto;
    },
    gif_request: function (url, data, callback) {
        var gif = document.createElement('img'), request = '?';
        for (var i in data) {
            request += i + '=' + escape(data[i]) + '&'
        }
        gif.width = 0;
        gif.height = 0;
        callback = this.gif_callback || function () {
            }
        gif.onerror = function () {
            setTimeout(function () {
                gif.parentElement.removeChild(gif);
            });
            callback();
        };
        gif.src = url + request;
        document.body.appendChild(gif)
    },
    script_request: function (data, method, id, url) {
        var s = document.createElement('script'), request = '?', url = url || this.url;
        for (var i in data) {
            if (typeof data[i] != 'undefined') request += i + '=' + escape(data[i]) + '&';
        }
        s.src = [url, this.org_id, method + request].join('/');
        s.type = 'text/javascript';
        s.id = id;
        document.body.appendChild(s);
    },
    add_cookie: function (key, value) {
        var supportsDoNotTrack = typeof $p_supports_do_not_track != 'undefined' && $p_supports_do_not_track
        var doNotTrack = window.doNotTrack === "1" || navigator.doNotTrack === "yes" || navigator.doNotTrack === "1" || navigator.msDoNotTrack === "1";
        if (supportsDoNotTrack && doNotTrack)return;
        var expire = new Date();
        expire.setDate(expire.getDate() + 7);
        if (typeof $p_session_only != 'undefined' && $p_session_only) value = escape(value); else value = escape(value) + "; expires=" + expire.toUTCString();
        value += ';domain=.' + document.location.host.split('.').slice(-2).join('.') + ';path=/';
        document.cookie = key + "=" + value;
    },
    variantContainer: function () {
        return {
            _to_string: function () {
                var string = []
                for (var i in this) {
                    if (i.slice(0, 1) != '_') string.push(this[i])
                }
                return string.join(',')
            }, _select_variants: function (button) {
                var selected_variants = this;
                for (var i in button) {
                    var variants = button[i], selection = Math.random(), top = 0
                    if (typeof selected_variants[i] == 'undefined' || selected_variants[i] == '') {
                        if (variants.length == 1) selected_variants[i] = variants[0].id; else {
                            for (var j = 0; j < variants.length; j++) {
                                var variant = variants[j];
                                top += variant.weight;
                                if (top > selection) {
                                    selected_variants[i] = variant.id;
                                    break;
                                }
                            }
                            ;
                        }
                    }
                }
                return selected_variants;
            }
        }
    },
    go: function () {
        var search = (document.location.search.toString().replace(/\?/, '') + document.location.hash.toString().replace(/\#/, '') + strTrim(window.name)).split('&'), cookies = document.cookie.split('; ')
        request = {}, preset_pages = {};
        this.url = this.url.replace(/http:/g, document.location.protocol.toString())
        this.cached_url = this.cached_url.replace(/http:/g, document.location.protocol.toString())
        for (var i = 0; i < search.length; i++) {
            var search_split = search[i].split('='), key = search_split[0], val = search_split[1];
            if (key.indexOf('sp_ref') !== -1) request.sp_ref = val;
            if (key.indexOf('_sp') !== -1) request.long_id = val;
            if (!request[key]) request[key] = val;
        }
        ;
        for (var i = cookies.length - 1; i >= 0; i--) {
            if (cookies[i].indexOf('_sp=') !== -1 && typeof request.long_id == 'undefined') request.long_id = cookies[i].split('=')[1];
            if (cookies[i].indexOf('_sp_var') !== -1) {
                var cookie = cookies[i].replace(/\s|;/, '').split('='), page = cookie[0].match(/\d.*/)[0], variants = unescape(cookie[1]).split(','), variants_object = new this.variantContainer;
                for (var j = variants.length - 1; j >= 0; j--) {
                    var share = variants[j].split(':')
                    variants_object[share[0]] = share[1];
                }
                ;
                preset_pages[page] = variants_object;
            }
        }
        ;
        var divs = document.body.getElementsByTagName('div'), pages = [], global_buttons = {};
        for (var i = divs.length - 1; i >= 0; i--) {
            var div = divs[i], the_classes = div.className, the_id = '', type = '';
            if (the_classes.match(/sp_\d*/)) {
                the_id = the_classes.match(/sp_\d*/)[0].replace(/sp_/g, '');
            }
            if (the_classes.replace('sp_share_button', '').match(/sp_[a-z][^\s]+/)) {
                type = the_classes.replace('sp_share_button', '').match(/sp_[a-z][^\s]+/)[0];
            }
            if (typeof this.templates[type] != 'undefined') {
                if (the_id == '') {
                    var page_url = div.getAttribute('page_url');
                    if (page_url == null) {
                        for (var j = document.head.childNodes.length - 1; j >= 0; j--) {
                            var node = document.head.childNodes[j];
                            if (node.nodeName == 'LINK') {
                                if (node.rel == 'canonical') page_url = node.href;
                            } else if (node.nodeName == 'META') {
                                var property = node.getAttribute('property'), content = node.getAttribute('content');
                                if (property == 'og:url') page_url = content;
                            }
                            if (page_url != null)break;
                        }
                        if (page_url == null) {
                            page_url = document.location.toString().replace(/sp\_ref\=(\w|.)+\&/g, '').replace(new RegExp($p_source_param + '\\=\(\\w|\\&\).'), '').split('#')[0]
                            if (page_url.slice(-1) == '?') page_url = page_url.slice(0, -1);
                        }
                    }
                    if (the_classes.indexOf('sp_share_button') !== -1) {
                        var threeDigitRandom = Math.floor(Math.random() * 1000), tmp_id = new Date().getTime() * 1000 + threeDigitRandom, label = escape(page_url) + '@' + escape(type);
                        this.templates[type](div)
                        if (typeof global_buttons[label] == 'undefined') {
                            var share_types = this.template_types[type], multiVariantCheck = this.multiVariantCheck(share_types), page = label + '@' + tmp_id + '@' + multiVariantCheck;
                            pages.push(page)
                            global_buttons[label] = tmp_id
                            this.buttons[tmp_id] = [div]
                        } else {
                            this.buttons[global_buttons[label]].push(div)
                        }
                    }
                } else {
                    var label = escape('manually_created_' + the_id) + '@' + escape(type)
                    this.templates[type](div)
                    if (typeof global_buttons[label] == 'undefined') {
                        var share_types = this.template_types[type], multiVariantCheck = this.multiVariantCheck(share_types), page = label + '@' + the_id + '@' + multiVariantCheck;
                        pages.push(page)
                        global_buttons[label] = the_id
                        if (type == 'sp_conversion')continue;
                        this.buttons[the_id] = [div]
                    } else {
                        this.buttons[global_buttons[label]].push(div)
                    }
                }
            }
        }
        if (pages.length > 0) request.p = pages.join('|');
        if (typeof request.sp_ref != 'undefined' || typeof request.p != 'undefined') this.script_request(request, 'v.js', 'shareprogress_logger');
        this.render_share_page();
        this.ready_fired = true;
        this.ready_callback();
    },
    fillInVariants: function (visits, sp_ref, long_id) {
        if (typeof visits != 'object')return false;
        var fix_callback = this.fix_callback, button_cookies_enabled = this.button_cookies;

        function get(id) {
            return document.getElementById(id);
        };
        function get_fix(id, link) {
            var el = get(id);
            el.href = link;
            return el;
        };
        function generate_default_share(type, page_url) {
            switch (type) {
                case'f':
                    return 'https://www.facebook.com/sharer/sharer.php?u=' + escape(page_url);
                case't':
                    var tweet = SharePop.getMetaTagData('sp:twitter_message');
                    tweet = tweet.concat(SharePop.getMetaTagData('sp:twitter'));
                    tweet = tweet.concat(SharePop.getMetaTagData('og:title'));
                    tweet = tweet.concat(SharePop.getMetaTagData('title'));
                    if (tweet.length == 0) tweet.push('');
                    return 'https://twitter.com/intent/tweet?text=' + escape(tweet[0].replace(/\{LINK\}/g, '') + ' ' + page_url);
                case'e':
                    var subject = SharePop.getMetaTagData('sp:email_subject');
                    subject = subject.concat(SharePop.getMetaTagData('og:title'));
                    subject = subject.concat(SharePop.getMetaTagData('title'));
                    if (subject.length == 0) subject.push('');
                    var body = SharePop.getMetaTagData('sp:email_body');
                    body = body.concat(SharePop.getMetaTagData('og:description'));
                    body = body.concat(SharePop.getMetaTagData('description'));
                    if (body.length == 0) body.push('');
                    return 'mailto:?subject=' + escape(subject[0]) + '&body=' + escape(body[0].replace(/\{LINK\}/g, '') + '\n\n' + page_url);
                default:
                    return page_url;
            }
        }

        function fix_dom(visits) {
            for (var i = 0; i < visits.length; i++) {
                var page = visits[i], buttons = SharePop.buttons[page.page_id];
                if (typeof buttons == 'undefined') {
                    buttons = SharePop.buttons[page.tmp_id] || [];
                }
                SharePop.shareRecord[page.page_id] = {};
                if (typeof page.to_variants != 'undefined' && button_cookies_enabled) {
                    SharePop.add_cookie('_sp_var_' + page.page_id, page.to_variants);
                }
                for (var j = buttons.length - 1; j >= 0; j--) {
                    var links = buttons[j].getElementsByTagName('a');
                    for (var k = links.length - 1; k >= 0; k--) {
                        var link = links[k];
                        type = link.getAttribute('type');
                        if (typeof page.default_share != 'undefined' && page.default_share) {
                            link.setAttribute('default_share', '1');
                            link.setAttribute('href', generate_default_share(type, page.page_url));
                            continue;
                        }
                        link.setAttribute('button', page.page_id);
                        link.setAttribute('visit', page.id);
                        link.setAttribute('variant', page[type].id);
                        link.setAttribute('href', page[type].link);
                        link.setAttribute('counter', page[type].shares_count);
                        if (type == 'f') {
                            link.setAttribute('thumbnail', page[type].thumbnail);
                            link.setAttribute('description', page[type].description);
                            link.setAttribute('title', page[type].title);
                            link.setAttribute('link', page[type].link);
                            link.setAttribute('href', SharePop.FB_Link(link));
                        }
                    }
                    var divs = buttons[j].getElementsByTagName('div');
                    for (var k = divs.length - 1; k >= 0; k--) {
                        var node = divs[k];
                        if (!node.getAttribute('counter'))continue;
                        var counter = node.getAttribute('counter'), counter_name = [counter, page.page_id].join('_');
                        node.setAttribute('id', page.page_id);
                        if (typeof SharePop.counters[counter_name] == 'undefined') SharePop.counters[counter_name] = [node]; else SharePop.counters[counter_name].push(node);
                    }
                }
            }
            var counter_data = [];
            for (var i = 0; i < visits.length; i++) {
                if (typeof visits[i].default_share != 'undefined' && visits[i].default_share)continue;
                var id = visits[i].page_id;
                counter_data.push(['f_' + id, visits[i].f.shares_count]);
                counter_data.push(['e_' + id, visits[i].e.shares_count]);
                counter_data.push(['t_' + id, visits[i].t.shares_count]);
            }
            SharePop.fillInCounters(counter_data);
            fix_callback(visits, sp_ref, long_id);
        }

        if (long_id) this.add_cookie('_sp', long_id);
        if (long_id) window.name += ' &_sp=' + long_id + '&';
        window.name = window.name.replace(/\&\_sp\_ref\_\d*=(\w|.)*/g, '');
        if (sp_ref) window.name += ' &' + sp_ref + '&';
        if (this.ready_fired) fix_dom(visits); else SharePop.ready_callback = function () {
            SharePop.ready_callback();
            fix_dom(visits)
        }
    },
    templates: {
        create: function (type) {
            return document.createElement(type)
        }, a: function (href) {
            var a = this.create('a');
            a.href = href;
            return a;
        }, img: function (src) {
            var img = this.create('img');
            img.src = src;
            return img;
        }, style: function (style_block) {
            var style = this.create('style');
            style.setAttribute('type', 'text/css');
            if (style.styleSheet) {
                style.styleSheet.cssText = style_block;
            }
            else {
                style.textContent = style_block;
            }
            return style;
        }, div: function () {
            return this.create('div');
        }
    },
    template_types: {},
    addTemplate: function (name, create, long_name, allowed_variants) {
        if (typeof create == 'string') {
            var create_function = function (el) {
                return el.innerHTML = create.replace(/\{SharePopURL\}/g, SharePop.url);
            }
        } else {
            var create_function = create;
        }
        this.templates[name] = function (el, id, variants) {
            this.go = create_function
            this.go(el);
            var nodes = el.childNodes;
            for (var i = nodes.length - 1; i >= 0; i--) {
                var node = nodes[i], type = node.nodeType == 1 ? node.getAttribute('type') : null, counter = node.nodeType == 1 ? node.getAttribute('counter') : null, id = el.className.match(/sp_\d*/)[0].replace(/sp_/g, '')
                if (type == 'e') node.onclick = function () {
                    return SharePop.Email(this);
                }
                if (type == 'f') node.onclick = function (event) {
                    if (event.preventDefault) event.preventDefault(); else event.returnValue = false;
                    SharePop.FB(this);
                }
                if (type == 't') node.onclick = function (event) {
                    if (event.preventDefault) event.preventDefault(); else event.returnValue = false;
                    SharePop.TW(this);
                };
                if (!!type && !!variants) {
                    var short_to_long = {f: 'facebook', e: 'email', t: 'twitter'}
                    node.setAttribute('variant', variants[short_to_long[type]])
                }
                if (!!type && !!id) node.setAttribute('button', id)
            }
            ;
        }
        this.template_types[name] = allowed_variants;
    },
    counters: {},
    neededCounters: function () {
        var ids = []
        for (var i in this.counters) {
            var id = this.counters[i][0].getAttribute('id')
            if (ids.indexOf(id) === -1) ids.push(id)
        }
        ;
        return ids.join('|');
    },
    updateCounter: function (count_elements, update_amount) {
        for (var j = count_elements.length - 1; j >= 0; j--) {
            var current_val = parseInt(count_elements[j].textContent || count_elements[j].innerText)
            current_val = isNaN(current_val) ? 0 : current_val;
            count_elements[j].textContent = current_val + parseInt(update_amount);
            count_elements[j].innerText = current_val + parseInt(update_amount);
        }
        ;
    },
    fillInCounters: function (new_counts) {
        var filled_counters = []
        for (var i in new_counts) {
            var count_type = new_counts[i][0], count = new_counts[i][1], count_elements = this.counters[count_type]
            if (typeof count_elements != 'undefined') {
                this.updateCounter(count_elements, count)
                filled_counters.push(count_type)
            }
        }
        ;
        for (var i in this.counters) {
            if (filled_counters.indexOf(i) === -1) {
                this.updateCounter(this.counters[i], 0);
            }
        }
        ;
    },
    multiVariantCheck: function (share_types) {
        if (share_types.indexOf('facebook') >= 0) {
            if (this.getMetaTagData('og:title').length > 1)return 1;
            if (this.getMetaTagData('og:title').length == 0 && this.getMetaTagData('title').length > 1)return 1;
            if (this.getMetaTagData('og:image').length > 1)return 1;
            if (this.getMetaTagData('og:description').length > 1)return 1;
            if (this.getMetaTagData('og:description').length == 0 && this.getMetaTagData('description').length > 1)return 1;
        }
        if (share_types.indexOf('twitter') >= 0) {
            if (this.getMetaTagData('sp:twitter_message').length > 1)return 1;
            if (this.getMetaTagData('sp:twitter_message').length == 0 && this.getMetaTagData('sp:twitter').length > 1)return 1;
        }
        if (share_types.indexOf('email') >= 0) {
            if (this.getMetaTagData('sp:email_subject').length > 1)return 1;
            if (this.getMetaTagData('sp:email_body').length > 1)return 1;
        }
        return 0;
    },
    getMetaTagData: function (name) {
        var tags = document.getElementsByTagName('meta'), data = [];
        for (var i = 0; i < tags.length; i++) {
            if (tags[i].getAttribute('name') == name || tags[i].getAttribute('property') == name) {
                data.push(tags[i].getAttribute('content'));
            }
        }
        return data;
    },
    isMobile: {
        Android: function () {
            return navigator.userAgent.match(/Android/i);
        }, BlackBerry: function () {
            return navigator.userAgent.match(/BlackBerry/i);
        }, iOS: function () {
            return navigator.userAgent.match(/iPhone|iPad|iPod/i);
        }, Opera: function () {
            return navigator.userAgent.match(/Opera Mini/i);
        }, Windows: function () {
            return navigator.userAgent.match(/IEMobile/i);
        }, any: function () {
            return (SharePop.isMobile.Android() || SharePop.isMobile.BlackBerry() || SharePop.isMobile.iOS() || SharePop.isMobile.Opera() || SharePop.isMobile.Windows());
        }
    },
    triggerShare: function (share_type, dom_object, visit_id, variant_id) {
        var my_event, element = window;
        if (document.createEvent) {
            my_event = document.createEvent("HTMLEvents");
            my_event.initEvent("share", true, true);
        } else {
            my_event = document.createEventObject();
            my_event.eventType = "share";
        }
        my_event.share = {share_type: share_type, dom_object: dom_object, visit_id: visit_id, variant_id: variant_id};
        if (document.createEvent) {
            element.dispatchEvent(my_event);
        } else {
            element.fireEvent("on" + event.eventType, my_event);
        }
    },
    render_share_page: function () {
        if (typeof sp_share_page != 'undefined') {
            var container = sp_share_page, iframe = document.createElement('iframe'), page_url = container.getAttribute('page_url') || (typeof sp_share_page_url === 'undefined' ? false : sp_share_page_url) || document.referrer
            if (page_url && page_url !== '') {
                var params = document.location.search.toString().replace(/\?/, ''), url = this.url.replace('http:', document.location.protocol) + '/' + this.org_id + '/page?page_url=' + escape(page_url) + '&extra=' + escape(params)
                iframe.setAttribute('src', url);
                iframe.scrolling = 'no'
                iframe.frameborder = '0'
                iframe.setAttribute('frameBorder', '0')
                iframe.allowTransparency = 'true'
                iframe.style.border = 'none'
                iframe.style.width = '100%'
                iframe.style.height = '100%'
                iframe.style.overflow = 'hidden'
                iframe.style.dislay = 'none'
                iframe.onload = function () {
                    iframe.style.display = 'block';
                    if (sp_share_page.offsetWidth >= 830) iframe.style.minHeight = '670px';
                    if (sp_share_page.offsetWidth < 830 && sp_share_page.offsetWidth >= 680) iframe.style.minHeight = '510px';
                    if (sp_share_page.offsetWidth < 680) iframe.style.minHeight = '470px';
                }
                container.appendChild(iframe)
            }
        }
    }
};
(function () {
    var ready = (function () {
        var ready_event_fired = false;
        var ready_event_listener = function (fn) {
            var idempotent_fn = function () {
                if (ready_event_fired) {
                    return;
                }
                ready_event_fired = true;
                return fn();
            }
            var do_scroll_check = function () {
                if (ready_event_fired) {
                    return;
                }
                try {
                    document.documentElement.doScroll('left');
                } catch (e) {
                    setTimeout(do_scroll_check, 1);
                    return;
                }
                return idempotent_fn();
            }
            if (document.readyState === "complete") {
                return idempotent_fn()
            }
            if (document.addEventListener) {
                document.addEventListener("DOMContentLoaded", idempotent_fn, false);
                window.addEventListener("load", idempotent_fn, false);
            } else if (document.attachEvent) {
                document.attachEvent("onreadystatechange", idempotent_fn);
                window.attachEvent("onload", idempotent_fn);
                var toplevel = false;
                try {
                    toplevel = window.frameElement == null;
                } catch (e) {
                }
                if (document.documentElement.doScroll && toplevel) {
                    return do_scroll_check();
                }
            }
        };
        return ready_event_listener;
    })();
    ready(function () {
        if (typeof SharePop.go != 'undefined') SharePop.go();
    });
})();
Array.prototype.indexOf = function (obj, start) {
    for (var i = (start || 0), j = this.length; i < j; i++) {
        if (this[i] === obj) {
            return i;
        }
    }
    return -1;
};
SharePop.addTemplate('sp_eft_horizontal', "<style type='text/css'>.sp_eft_horizontal { display: inline-block; background: url('http://c.shpg.org/images/share_horz_email_facebook_twitter_sidetitle.png') no-repeat top left; width: 350px; height: 71px; text-decoration:none; text-align:left;}.sp_eft_horizontal #elink {  display: inline-block;  width: 40px;  height: 41px;  background: #14AC2B url('http://c.shpg.org/images/share_icon_email.png') no-repeat top left;  margin: 15px 0 0 21px;}.sp_eft_horizontal #flink {  display: inline-block;  width: 40px;  height: 41px;  background: #39579A url('http://c.shpg.org/images/share_icon_facebook.png') no-repeat top left;  margin: 15px 0 0 11px;}.sp_eft_horizontal #tlink {  display: inline-block;  width: 40px;  height: 41px;  background: #02ACED url('http://c.shpg.org/images/share_icon_twitter.png') no-repeat top left;  margin: 15px 0 0 12px;}</style><a href='#' id='elink' type='e'></a><a href='#' id='flink' type='f'></a><a href='#' id='tlink' type='t'></a>", 'Horizontal Share Buttons', ['email', 'facebook', 'twitter']);
SharePop.addTemplate('sp_fb_small', "<style type='text/css'>.sp_fb_small a {  display: inline-block;  width: 33px;  height: 33px;  background:  #39579A url({SharePopURL}/images/icon-fb.gif) no-repeat top left;}</style><a type='f' stype='standard' href='#'></a>", 'Small Facebook Button', ['facebook']);
SharePop.addTemplate('sp_tw_small', "<style type='text/css'>.sp_tw_small a {  display: inline-block;  width: 33px;  height: 33px;  background:  #02ACED url({SharePopURL}/images/icon-twitter.gif) no-repeat top left;}</style><a type='t' href='#'></a>", 'Small Twitter Button', ['twitter']);
SharePop.addTemplate('sp_fb_large', "<style type='text/css'>.sp_fb_large a {   display: inline-block;   width: 213px;   height: 30px;   color: #FFFFFF;   text-decoration: none;   text-align: center;   padding: 15px 0 0;   background: #39579A;   font: normal 17px/17px Arial, Helvetica, sans-serif;   box-sizing: content-box;   -webkit-box-sizing: content-box;   -moz-box-sizing: content-box; }</style><a type='f' stype='standard' href='#'>Post To Facebook &#9658;</a>", 'Large Facebook Button', ['facebook']);
SharePop.addTemplate('sp_em_large', "<style type='text/css'>.sp_em_large a { 	display: inline-block;	width: 213px;	height: 30px; 	color: #FFFFFF;	text-decoration: none;	text-align: center;	padding: 15px 0 0;	background: #14AC2B;	font: normal 17px/17px Arial, Helvetica, sans-serif;	box-sizing: content-box;	  -webkit-box-sizing: content-box;	  -moz-box-sizing: content-box;} </style><a type='e' href='#'>Share By Email &#9658;</a>", 'Large Email Button', ['email']);
SharePop.addTemplate('sp_tw_large', "<style type='text/css'>.sp_tw_large a {  display: inline-block;  width: 213px;  height: 30px;  color: #FFFFFF;  text-decoration: none;  text-align: center;  padding: 15px 0 0;  background: #02ACED;  font: normal 17px/17px Arial, Helvetica, sans-serif;  box-sizing: content-box;  -webkit-box-sizing: content-box;  -moz-box-sizing: content-box;}</style><a type='t' href='#'>Share On Twitter &#9658;</a>", 'Large Twitter Button', ['twitter']);
SharePop.addTemplate('sp_em_small', "<style type='text/css'>.sp_em_small a {  display: inline-block;  width: 33px;  height: 33px;  background:  #14AC2B url({SharePopURL}/images/icon-email.gif) no-repeat top left;}</style><a type='e' href='#'></a>", 'Small Email Button', ['email']);
SharePop.addTemplate('sp_fb_big_large', "<style type='text/css'>.sp_fb_big_large a {   display: inline-block;   width: 213px;   height: 30px;   color: #FFFFFF;   text-decoration: none;   text-align: center;   padding: 15px 0 0;   background: #39579A;   font: normal 17px/17px Arial, Helvetica, sans-serif;   box-sizing: content-box;   -webkit-box-sizing: content-box;   -moz-box-sizing: content-box; }</style><a type='f' stype='standard' href='#'>Post To Facebook &#9658;</a>", 'Large Facebook Button with Wide Image', ['facebook']);
SharePop.addTemplate('sp_eft_horizontal_cnt', "<style type='text/css'>.sp_eft_horizontal_cnt { display: inline-block; background: url('http://c.shpg.org/images/share_horz_email_facebook_twitter_title_counter.png') no-repeat top left; width: 186px; height: 104px; text-align:left;}.sp_eft_horizontal_cnt a, .sp_eft_horizontal_cnt div, .sp_eft_horizontal_cnt {	margin: 0;     padding: 0;     line-height: 1;     opacity: 1;     clear: none;}.sp_eft_horizontal_cnt div {	font-family: Helvetica;	font-size: 10px;	text-align: center;	float: left;	width: 36px;	height: 12px;	line-height: 1em;	margin-top: 8px;	background: white;	border: 2px solid #efefef;} .sp_eft_horizontal_cnt #elink {  display: inline-block;  width: 40px;  height: 41px;  background: #14AC2B url('http://c.shpg.org/images/share_icon_email.png') no-repeat top left;  margin: 31px 0 0 21px;}.sp_eft_horizontal_cnt #ecounter {  margin-left: 21px; }.sp_eft_horizontal_cnt #flink {  display: inline-block;  width: 40px;  height: 41px;  background: #39579A url('http://c.shpg.org/images/share_icon_facebook.png') no-repeat top left;  margin: 31px 0 0 11px;}.sp_eft_horizontal_cnt #fcounter {  margin-left: 11px; }.sp_eft_horizontal_cnt #tlink {  display: inline-block;  width: 40px;  height: 41px;  background: #02ACED url('http://c.shpg.org/images/share_icon_twitter.png') no-repeat top left;  margin: 31px 0 0 12px;}.sp_eft_horizontal_cnt #tcounter {  margin-left: 12px; }</style><a href='#' id='elink' type='e'></a><a href='#' id='flink' type='f'></a><a href='#' id='tlink' type='t'></a><div counter='e' id='ecounter'></div><div counter='f' id='fcounter'></div><div counter='t' id='tcounter'></div>", 'Horizontal Share Buttons with Counters', ['email', 'facebook', 'twitter']);
SharePop.addTemplate('sp_e_small_cnt', "<style type='text/css'>.sp_e_small_cnt a {  display: inline-block;  width: 33px;  height: 33px;  background:  #14AC2B url({SharePopURL}/images/icon-email.gif) no-repeat top left;}.sp_e_small_cnt div { display: inline-block; border: 1px #888 solid; color: #888; font-family: Arial, sans-serif; font-size: 12px; line-height: 1.5em; width: auto; margin-left: 5px; top: -13px; position: relative; margin-left: 5px; height: 33px; padding: 6px; box-sizing: border-box;}</style><a href='#' id='elink' type='e'></a><div counter='e' id='ecounter'></div>", 'Small Email Share Button with Counter', ['email']);
SharePop.addTemplate('sp_tw_small_cnt', "<style type='text/css'>.sp_tw_small_cnt a {  display: inline-block;  width: 33px;  height: 33px;  background:  #02ACED url({SharePopURL}/images/icon-twitter.gif) no-repeat top;}.sp_tw_small_cnt div { display: inline-block; border: 1px #888 solid; color: #888; font-family: Arial, sans-serif; font-size: 12px; line-height: 1.5em; width: auto; margin-left: 5px; top: -13px; position: relative; margin-left: 5px; height: 33px; padding: 6px; box-sizing: border-box;}</style><a href='#' id='tlink' type='t'></a><div counter='t' id='tcounter'></div>", 'Small Twitter Share Button with Counter', ['twitter']);
SharePop.addTemplate('sp_fb_small_cnt', "<style type='text/css'>.sp_fb_small_cnt a {  display: inline-block;  width: 33px;  height: 33px;  background:  #39579A url({SharePopURL}/images/icon-fb.gif) no-repeat top left;}.sp_fb_small_cnt div { display: inline-block; border: 1px #888 solid; color: #888; font-family: Arial, sans-serif; font-size: 12px; line-height: 1.5em; width: auto; margin-left: 5px; top: -13px; position: relative; margin-left: 5px; height: 33px; padding: 6px; box-sizing: border-box;}</style><a href='#' stype='standard' id='flink' type='f'></a><div counter='f' id='fcounter'></div>", 'Small Facebook Share Button with Counter', ['facebook']);
SharePop.addTemplate('sp_conversion', '', 'Share Button Conversion', []);
