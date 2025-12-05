import moment from "moment/moment";

export function unwrapSuccess(enveloped) {
    if (enveloped && enveloped.success) {
        if (enveloped.meta && Object.keys(enveloped.meta).length > 0) {
            enveloped.data.meta = enveloped.meta;
        }
        return enveloped.data;
    }
    return enveloped;
}

export function toLocalInputValue(isoString) {
    if (!isoString) {
        return '';
    }
    return isoString.substring(0, 16);
}

export function formatLocal(isoString) {
    if (!isoString) {
        return '';
    }
    return moment(isoString).format('DD.MM.Y H:m');
}

export function nowLocal() {
    return moment().format().substring(0, 16);
}
