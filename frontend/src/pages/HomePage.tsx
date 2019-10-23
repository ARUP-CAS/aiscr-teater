import React from 'react'
import gql from 'graphql-tag'
import { useQuery } from 'react-apollo-hooks'
import 'scss/pages/_page_home.scss'
import { Page } from 'components/Page'
import { LargeHeader } from 'components/LargeHeader'
import { Content } from 'components/Content'
import { GetHomePage } from 'generated/GetHomePage'
import Categories from 'components/categories'
import { OoopsError } from '../components/Ooops'
import { LargeHeaderContainer } from '../containers/LargeHeaderContainer'
import { Partners } from 'components/Partners'

const GET_HOME_PAGE = gql`
  query GetHomePage {
    listRootCategories {
      id
      name
      url
    }
  }
`

export default function HomePage(): JSX.Element {
  const { data, loading, error } = useQuery<GetHomePage>(GET_HOME_PAGE)

  if (error) {
    return (
      <Page>
        <LargeHeader title="Domov" />
        <Content className="content-home">
          <OoopsError />
        </Content>
      </Page>
    )
  }

  return (
    <Page>
      <LargeHeaderContainer title="Domov" />
      <Content className="content-home">
        <p className="description">
          Tezaurus archeologické terminologie je určen širokému okruhu uživatelů
          jak z řad odborné tak laické veřejnosti se zájmem o archeologii, od
          poučených laiků, přes amatérské archeology, nové studenty archeologie,
          knihovníky archeologických institucí a profesionální archeology.
          Účelem tezauru je zpřístupnit těmto skupinám uživatelů archeologickou
          terminologii. Protože primární, ale nikoli výlučný, okruh uživatelů
          budou knihovníci, je tezaurus tvořen částečně předmětovými hesly
          národních autorit spolu s dalšími odborně-archeologickými termíny,
          které se v rejstříku národních autorit nenachází. Tezaurus obsahuje
          také hesla z příbuzných oborů a dalších pomocně-historických
          kategorií. Hesla v tezauru jsou uspořádána hierarchicky a celou
          strukturou jde volně procházet za použití Kategorií níže. Alternativně
          lze hesla full-textově vyhledávat. Další informace ohledně možností
          využití tezauru různými skupinami uživatelů, o vyhledávací aplikaci,
          struktuře a výběru hesel se nacházejí v poli Nápověda.
        </p>
        <h2>Prozkoumejte další oblasti</h2>
        <Categories
          data={data ? data.listRootCategories : []}
          loading={loading}
        />
        <Partners />
      </Content>
    </Page>
  )
}
