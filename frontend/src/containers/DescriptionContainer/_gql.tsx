import gql from 'graphql-tag'

export const GET_DESCRIPTION_PAGE = gql`
  query GetDescriptionPage($url: String!) {
    categoryBreadcrumbsWithUrl(url: $url) {
      name
      url
    }
    singleCategoryWithUrl(url: $url) {
      id
      name
      description
      children {
        id
        name
        categoryType
        url
        isLeaf
      }
      parentsNeighbors {
        id
        name
        categoryType
        url
        isLeaf
      }
      neighbors {
        id
        name
        categoryType
        url
        isLeaf
      }
    }
  }
`
