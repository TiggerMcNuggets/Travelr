


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

export const getDepthData = (depth) => {
    const moduloDepth = depth % 3;
    const data = {large: false, number: depth + 1};
    if (moduloDepth === 0) {
        data.color = "#536DFE";
        data.large = true;
    }
    else if (moduloDepth === 1) {
        data.color = "#3D5AFE";
    }
    else {
        data.color = "#304FFE";
    }
    return data;
};