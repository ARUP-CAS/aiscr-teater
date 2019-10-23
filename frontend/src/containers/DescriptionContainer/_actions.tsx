import { GetDescriptionPage } from '../../generated/GetDescriptionPage'
import { ColumnItem } from '../../components/column/_types'
import { CategoryType } from '../../generated/globalTypes'

export function parseBreadcrumbsData(data?: GetDescriptionPage): ColumnItem[] {
  const breadcrumbsLength = data ? data.categoryBreadcrumbsWithUrl.length : 0

  return (data ? [...data.categoryBreadcrumbsWithUrl] : [])
    .reverse()
    .map((breadcrumb, index) => ({
      ...breadcrumb,
      active: !!(index === breadcrumbsLength - 1)
    }))
}

function convertCategoryTypeToColor(categoryType: CategoryType): string {
  switch (categoryType) {
    case CategoryType.LOCAL:
      return '#ffa500'
    case CategoryType.NATIONAL:
      return '#51804e'
    case CategoryType.BLANK:
      return '#3096f6'
    default:
      return '#bc4253'
  }
}

function parseData(
  key: 'neighbors' | 'children' | 'parentsNeighbors',
  data?: GetDescriptionPage
): ColumnItem[] {
  if (!data) {
    return []
  }

  return data.singleCategoryWithUrl[key].map(category => ({
    ...category,
    color: convertCategoryTypeToColor(category.categoryType),
    active: !!(data && category.id === data.singleCategoryWithUrl.id)
  }))
}

export function parseNeighborsData(data?: GetDescriptionPage): ColumnItem[] {
  return parseData('neighbors', data)
}

export function parseChilrenData(data?: GetDescriptionPage): ColumnItem[] {
  return parseData('children', data)
}

export function parseParentsData(data?: GetDescriptionPage): ColumnItem[] {
  return parseData('parentsNeighbors', data)
}
