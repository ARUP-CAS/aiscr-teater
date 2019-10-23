export function isOutOfViewport<T extends HTMLElement>(
  elem: T
): {
  any: boolean
  all: boolean
} {
  const bounding = elem.getBoundingClientRect()

  var out = {
    top: bounding.top < 0,
    left: bounding.left < 0,
    bottom:
      bounding.bottom >
      (window.innerHeight || document.documentElement.clientHeight),
    right:
      bounding.right >
      (window.innerWidth || document.documentElement.clientWidth)
  }

  return {
    any: out.top || out.left || out.bottom || out.right,
    all: out.top && out.left && out.bottom && out.right
  }
}
