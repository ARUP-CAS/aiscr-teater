export function convertLocalLinksToNewTab(
  parentElement?: HTMLElement | null
): void {
  if (!parentElement) {
    return
  }

  const links = parentElement.getElementsByTagName('a')
  for (let i = 0; i < links.length; i++) {
    links[i].target = '_blank'
  }
}
