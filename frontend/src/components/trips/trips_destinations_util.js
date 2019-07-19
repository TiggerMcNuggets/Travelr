


export const setOrdinal = (destinations) => {
    const dests = [...destinations];
    dests.forEach((d, i) => {
        d.ordinal = i;
    });
    return dests;
};

export const getChildrenCount = (destinations, parent) => {
    let i = parent.ordinal + 1;
    while (i < destinations.length && destinations[i].depth > parent.depth) {
        i += 1;
    }
    return i - parent.ordinal - 1;
};