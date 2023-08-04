export const isOutOfViewport = <T extends HTMLElement>(elem: T) => {
  const bounding = elem.getBoundingClientRect();

  const out = {
    top: bounding.top < 0,
    left: bounding.left < 0,
    bottom:
      bounding.bottom >
      (window.innerHeight || document.documentElement.clientHeight),
    right:
      bounding.right >
      (window.innerWidth || document.documentElement.clientWidth)
  };

  return {
    any: out.top || out.left || out.bottom || out.right,
    all: out.top && out.left && out.bottom && out.right
  };
};
