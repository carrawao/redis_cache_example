'use strict';

import { wrap } from 'rest';
import defaultRequest from 'rest/interceptor/defaultRequest';
import mime from 'rest/interceptor/mime';
import uriTemplateInterceptor from './api/uriTemplateInterceptor';
import errorCode from 'rest/interceptor/errorCode';
import { child } from 'rest/mime/registry';

const registry = child();

registry.register('text/uri-list', require('./api/uriListConverter'));

export default wrap(mime, { registry: registry })
		.wrap(uriTemplateInterceptor)
		.wrap(errorCode)
		.wrap(defaultRequest, { headers: {'Accept': 'application/json', 'Content-Type': 'application/json; charset=utf-8'}});