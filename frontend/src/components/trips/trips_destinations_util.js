


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
        data.color = "blue";
        data.large = true;
    }
    else if (moduloDepth === 1) {
        data.color = "red";
    }
    else {
        data.color = "green";
    }
    return data;
};

export const isPromotable = (destinations, index) => {
    if (index === 0) return false;
    const child = destinations[index];
    const parent = destinations[index - 1];
    return (child.depth - parent.depth) < 1;
};

// TODO: find the right balance
export const isDemotable = (destinations, index) => {
    console.log(destinations[index].depth);
    console.log("parmas", destinations, index);
    if (destinations[index].depth === 0) return false;
    if ((destinations.length - 1) === index) return true;
    const child = destinations[index + 1];
    return (child.depth - parent.depth) >= 1;

};