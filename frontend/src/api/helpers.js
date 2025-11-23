import moment from "moment/moment";

export function unwrapSuccess(enveloped) {
    if (enveloped && enveloped.success) return enveloped.data;
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
    const d = new Date();
    const pad = (n) => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
}
